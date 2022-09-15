package com.wordCloudCore.controller;

import com.wordCloudCore.models.database_models.TextFile;
import com.wordCloudCore.models.mq_models.TextProcessMessage;
import com.wordCloudCore.models.database_models.TextProcessProgress;
import com.wordCloudCore.repository.TextFileRepository;
import com.wordCloudCore.repository.TextProcessProgressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;

@CrossOrigin(origins="http://localhost:8000")
@RestController
public class FileUpload {

    private static final Logger log = LoggerFactory.getLogger(FileUpload.class);
    private static final StopWatch sw = new org.springframework.util.StopWatch();

    /**
     * Field, that gets all the microservices queue exchange and routing key values from the "application.properties".
     */
    @Value("#{'${text_service_queues}'.split(',')}")
    String[] text_process_queues;

    @Autowired
    TextProcessProgressRepository textProcessProgressRepository;
    @Autowired
    TextFileRepository textFileRepository;
    @Autowired
    private RabbitTemplate template;

    /**
     * Endpoint which allows user to upload text file. Endpoint allows user to send words that he wants to omit during the
     * text process and choose if he wants words with spelling error as a processing result or not.
     */

    @PostMapping(value = "/upload_txt_file")
    public String uploadFile(@RequestParam("file") MultipartFile text_file, @RequestHeader("omitted-words") String omitted_words, @RequestHeader("possible-typos") boolean possible_typos) throws IOException {
        ArrayList<String> text_for_processing = capsulate_text(text_file.getInputStream());

        //Create MQ message object.
        String uniqueMessageId = Objects.requireNonNull(text_file.getOriginalFilename()).replace(".txt", "") + "-" + UUID.randomUUID().toString().substring(0, 8);
        TextProcessMessage message = new TextProcessMessage();
        message.setMessageId(uniqueMessageId);
        message.setOmitted_words(omitted_words);
        message.setPossible_typos(possible_typos);

        //Store uploaded text file metadata and create a new text process row in the "textprocessprogress" table.
        textFileRepository.save(new TextFile(uniqueMessageId, text_file.getOriginalFilename(), Instant.now()));
        textProcessProgressRepository.save(new TextProcessProgress(uniqueMessageId, text_for_processing.size(), 0));

        int queueNr = 0;
        for (String s : text_for_processing) {
            String[] queue_details = text_process_queues[queueNr].split(":");

            message.setFile_content(s);
            template.convertAndSend(queue_details[0], queue_details[1], message);

            //If queueNr is bigger than text_process_queues array, then go back to the start. Else, just keep adding up.
            queueNr = queueNr < text_process_queues.length - 1 ? queueNr + 1 : 0;
        }


        return uniqueMessageId + ":" + text_for_processing.size();
    }

    /**
     * "capsulate_text" takes text file stream which will be used to read lines from the text files. This method make message capsules for
     * the text processing service. Capsules that are made by this method will be stored in Arraylist. Each capsule contains 500 lines of the
     * text file.
     * @param text_Stream InputStream
     * @return ArrayList which contains RabbitMQ message capsules.
     */
    public static ArrayList<String> capsulate_text(InputStream text_Stream){
        ArrayList<String> capsulated_texts = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(text_Stream));

        String line;
        int line_count = 0;
        try {
            while ((line = br.readLine()) != null) {
                if(line.length() > 0) {
                    if (line_count == 2000) {
                        capsulated_texts.add(sb.toString());
                        sb.setLength(0);
                        line_count = 0;
                    } else {
                        sb.append(line).append(System.lineSeparator());
                        line_count++;
                    }
                }
            }
            capsulated_texts.add(sb.toString());
            sb.setLength(0);
        } catch (IOException e){
            System.out.println(e);
        }

        return capsulated_texts;
    }
}

package com.wordCloudCore.controller;

import com.wordCloudCore.config.MQConfig;
import com.wordCloudCore.models.TextProcessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUpload {

    private static final Logger log = LoggerFactory.getLogger(FileUpload.class);
    private static final StopWatch sw = new org.springframework.util.StopWatch();

    @Autowired
    private RabbitTemplate template;

    @PostMapping(value = "/upload_txt_file")
    public void uploadFile(@RequestParam("file") MultipartFile text_file) throws IOException {
        sw.start("text_for_processing - " + text_file.getOriginalFilename());
        ArrayList<String> text_for_processing = capsulate_text(text_file.getInputStream());
        sw.stop();

        log.info("Time taken by the last task: " + sw.getLastTaskName() + ":" + (float) sw.getLastTaskTimeMillis() / 1000 + "sec");
        log.info("Capsuled text count: " + text_for_processing.size() + "\n");

        TextProcessMessage message = new TextProcessMessage();

        String uniqueMessageId = UUID.randomUUID().toString().substring(8);
        Date currentDate = new Date();
        message.setMessageId(uniqueMessageId);
        message.setFileName(text_file.getOriginalFilename());
        message.setMessageDate(currentDate);
        for (int i = 0; i < text_for_processing.size(); i++) {
            message.setFile_content(text_for_processing.get(i));
            template.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, message);
        }
    }

    public static ArrayList<String> capsulate_text(InputStream text_Stream){
        ArrayList<String> capsulated_texts = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(text_Stream));

        String line;
        int line_count = 0;
        try {
            while ((line = br.readLine()) != null) {
                if(line_count == 100){
                   capsulated_texts.add(sb.toString());
                   sb.setLength(0);
                   line_count = 0;
                } else {
                    sb.append(line).append(System.lineSeparator());
                    line_count++;
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }

        return capsulated_texts;
    }

}

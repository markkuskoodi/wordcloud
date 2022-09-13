package com.wordCloudTextProcess.controller;

import com.wordCloudTextProcess.models.TextProcess;
import com.wordCloudTextProcess.models.TextProcessed;
import com.wordCloudTextProcess.repository.TextProcessRepository;
import com.wordCloudTextProcess.models.TextProcessMessage;
import com.wordCloudTextProcess.service.WordCheck;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Component
public class TextProcessController {

    @Value("${text_processed.rabbitmq.exchange}")
    private String TEXT_PROCESSED_EXCHANGE;
    @Value("${text_processed.rabbitmq.routingkey}")
    private String TEXT_PROCESSED_ROUTINGKEY;

    @Autowired
    TextProcessRepository textProcessRepository;
    @Autowired
    WordCheck wordCheck;
    @Autowired
    private RabbitTemplate template;

    private Set<String> omitted_words;

    private Pattern pattern = Pattern.compile("([a-zA-Z]([a-z'A-Z]*[a-zA-Z])?)");

    @RabbitListener(queues = "${text_process_service.rabbitmq.queue}")
    public void listener(TextProcessMessage message){
        try {
            System.out.println(message.isPossible_typos());
            setOmitted_words(message.getOmitted_words());
            process_text(message);
            template.convertAndSend(TEXT_PROCESSED_EXCHANGE, TEXT_PROCESSED_ROUTINGKEY, new TextProcessed(message.getMessageId(), "processed"));
        } catch (Exception e){
            template.convertAndSend(TEXT_PROCESSED_EXCHANGE, TEXT_PROCESSED_ROUTINGKEY, new TextProcessed(message.getMessageId(), "processing_failed"));
        }
    }

    public void setOmitted_words(String omitted_words){
        this.omitted_words = new HashSet<String>();
        String[] words = omitted_words.split(",");
        for (String s : words){
            this.omitted_words.add(s.replaceAll("[^a-zA-Z']+", "").toLowerCase());
        }
    }

    public void process_text(TextProcessMessage text_to_process){
        List<TextProcess> textProcesses = new ArrayList<>();

        String[] all_words = pattern
                .matcher(text_to_process.getFile_content().strip())
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);


        for (String s : all_words) {
            s = s.toLowerCase();
            if(!omitted_words.contains(s) && !s.equals("")){
                if(text_to_process.isPossible_typos()){
                    if(!wordCheck.checkIfExists(s)) {
                        textProcesses.add(new TextProcess(s, text_to_process.getMessageId()));
                    }
                } else {
                    textProcesses.add(new TextProcess(s, text_to_process.getMessageId()));
                }
            }
        }
        textProcessRepository.saveAll(textProcesses);
    }
}

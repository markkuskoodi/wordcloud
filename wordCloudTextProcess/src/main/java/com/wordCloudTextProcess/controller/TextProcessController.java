package com.wordCloudTextProcess.controller;

import com.wordCloudTextProcess.config.MQConfig;
import com.wordCloudTextProcess.models.TextProcess;
import com.wordCloudTextProcess.repository.TextProcessRepository;
import com.wordCloudTextProcess.models.TextProcessMessage;
import com.wordCloudTextProcess.service.WordCheck;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class TextProcessController {

    @Autowired
    TextProcessRepository textProcessRepository;

    @Autowired
    WordCheck wordCheck;
    private static final Set<String> articles_conjunctions = Set.of("and", "or", "the", "a", "an");

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(TextProcessMessage message){
        process_text(message);
    }

    public void process_text(TextProcessMessage text_to_process){
        String special_chars_removed = text_to_process.getFile_content().strip().replaceAll("\\R+", " ");

        String[] spaces_removed = special_chars_removed.split("\\s+");

        for (String s : spaces_removed) {
            String word = s.replaceAll("[^a-zA-Z']+", "").toLowerCase();
            if(!articles_conjunctions.contains(word) && !word.equals("")){
                if(wordCheck.checkIfExists(word)){
                    textProcessRepository.save(new TextProcess(word, text_to_process.getMessageId(), false));
                } else {
                    textProcessRepository.save(new TextProcess(word, text_to_process.getMessageId(), true));
                }
            }
        }
    }
}

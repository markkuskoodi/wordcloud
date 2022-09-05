package com.wordCloudTextProcess.controller;

import com.wordCloudTextProcess.config.MQConfig;
import models.TextProcessMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TextProcess {

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(TextProcessMessage message){
        System.out.println(message);
    }

    public void process_text(String text_to_process){

    }
}

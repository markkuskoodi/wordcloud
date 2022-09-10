package com.wordCloudTextProcess.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Service
public class WordCheck {

    private static Logger logger = LoggerFactory.getLogger(WordCheck.class);

    private String[] dictonary_files = new String[]{"dictionary.txt", "words.txt", "words_2.txt"};

    private Set<String> dictionary = new HashSet<>();

    public WordCheck(){}

    public Boolean checkIfExists(String word){
        return dictionary.contains(word);
    }

    @PostConstruct
    public void init(){
        for (String file_name:dictonary_files){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("spell_check_words/" + file_name).getInputStream()));
                String line;

                while ((line = br.readLine()) != null){
                    dictionary.add(line.toLowerCase().strip());
                }
            } catch (IOException e) {
                logger.info(String.valueOf(e));
            }
        }
    }

}

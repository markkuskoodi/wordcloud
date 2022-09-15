package com.wordCloudTextProcess.component;

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

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * This is the main component of this text process service. It is responsible for setting the omitted words, finding the
 * words in given string and inserting them into the database. It also has a setting that allows user to only find
 * possible spelling errors in the text.
 */

@Component
public class TextProcessComponent {

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

    /**
     * Set of already existing words that doesn't need to be in the statistics because they don't really add any
     * meaning to the WordCloud.
     */
    private Set<String> omitted_words = new HashSet<>(Arrays.asList("i","a","about", "an","and","are","as","at",
            "be", "been","by","com","for", "from","how","in",
            "is","it","not", "of","on","or","that",
            "the","this","to","was", "what","when","where", "which",
            "who","will","with", "www", "we", "us", "our", "ours",
            "they", "them", "their", "he", "him", "his",
            "she", "her", "hers", "its", "you", "yours", "your",
            "has", "have", "would", "could", "should", "shall",
            "can", "may", "if", "then", "else", "but",
            "there", "these", "those", "i'm", "i've", "no", "yes"));

    /**
     * Regex pattern, which will be used for finding all the words in a given string. It will be compiled during
     * this service start up because then it won't need to compile everytime this service looks for the words in the
     * string.
     */
    private Pattern pattern = Pattern.compile("([a-zA-Z]([a-z'A-Z]*[a-zA-Z])?)");

    /**
     * Method that listens to a RabbitMQ queue, where WordCloudCore sends all the text capsules that needs to be
     * processed. It sets user given words that needs to be omitted and then sends the text capsule to text processing.
     * If text process completes/fails, it sends the message to the WordCloudCore with status "processed"/"processing_failed".
     */
    @RabbitListener(queues = "${text_process_service.rabbitmq.queue}")
    public void listener(TextProcessMessage message){
        try {
            setOmitted_words(message.getOmitted_words());
            process_text(message);
            template.convertAndSend(TEXT_PROCESSED_EXCHANGE, TEXT_PROCESSED_ROUTINGKEY, new TextProcessed(message.getMessageId(), "processed"));
        } catch (Exception e){
            template.convertAndSend(TEXT_PROCESSED_EXCHANGE, TEXT_PROCESSED_ROUTINGKEY, new TextProcessed(message.getMessageId(), "processing_failed"));
        }
    }

    /**
     * Adds user sent words to the set of words which will be left out of the text processing statistics.
     */
    public void setOmitted_words(String omitted_words){
        String[] words = omitted_words.split(",");
        System.out.println(Arrays.asList(words));
        for (String s : words){
            this.omitted_words.add(s.replaceAll("[^a-zA-Z']+", "").toLowerCase());
        }
    }

    /**
     * This method finds all the words in given string by using regex pattern. Words that have been found will be
     * stored in String array, which will be later looped over. If the word in the array meets the condition, it will
     * be inserted into the database.
     * The condition that user can set:
     *  - Find all the spelling errors / Ignore spelling errors
     *  - Words that user doesn't want to include in the statistics
     */

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

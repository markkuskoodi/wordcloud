package com.wordCloudCore.component;

import com.wordCloudCore.models.database_models.TextProcessProgress;
import com.wordCloudCore.models.mq_models.TextProcessed;
import com.wordCloudCore.repository.TextProcessProgressRepository;
import com.wordCloudCore.repository.TextProcessRepository;
import com.wordCloudCore.repository.TextResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Optional;

/**
 * WordCloudCore component which job is to listen MQ messages and update the "textprocessprogress"
 * table accordingly. If the processing of the text file ends, this component makes the query to the "textresult"
 * table which counts each word in the "textprocess" table and inserts the result into the "textresult". After
 * that, this component makes query to delete all rows, which are related to the text file that was processed, in the
 * "textprocess" table.
 */

@Component
public class TextProcessedQueue {

    private static final Logger log = LoggerFactory.getLogger(TextProcessedQueue.class);
    private static final StopWatch sw = new org.springframework.util.StopWatch();
    @Autowired
    TextProcessProgressRepository textProcessProgressRepository;

    @Autowired
    TextProcessRepository textProcessRepository;

    @Autowired
    TextResultRepository textResultRepository;

    /**
     * "consumeMessage" listens to the all the messages that WordCloudTextProcess sends. Received messages will be sent
     * to the "changeCurrentlyProcessed".
     */
    @RabbitListener(queues = "${text_processed.rabbitmq.queue}")
    public void consumeMessage(TextProcessed textProcessed){
        try {
            changeCurrentlyProcessed(textProcessed.getFile_id(), textProcessed.getStatus().equals("processed"));
        } catch (Exception e) {
            log.info(String.valueOf(e));
        }
    }

    /**
     * Method, that updates the progress of the text file processing in the database. If text file processing ends,
     * this method also makes sure that the "textresult" table gets all the word counts from the "textprocess". After that,
     * all the rows that are related to the text file will be deleted in the "textprocess" table.
     */
    public void changeCurrentlyProcessed(String file_id, boolean processed) throws Exception {
        Optional<TextProcessProgress> current_progress = textProcessProgressRepository.findByTextfileid(file_id);
        if(current_progress.isPresent()) {
            long new_progress_count = processed ? current_progress.get().getCurrently_processed() + 1 : -1;

            if(new_progress_count == current_progress.get().getCapsuled_message_count()){
                sw.start("Inserting to TextResult and deleting from Textprocess - " + file_id);
                textResultRepository.insertTextFileResults(file_id);
                textProcessRepository.deleteTextProcessFieldsByFileId(file_id);
                sw.stop();
                log.info(sw.getLastTaskName() + ": " + sw.getLastTaskTimeMillis() / 1000 + "sec");
            }

            textProcessProgressRepository.setCountProgress(file_id, new_progress_count);
        } else {
            throw new Exception("This text process progress doesn't exist in the database.");
        }
    }
}

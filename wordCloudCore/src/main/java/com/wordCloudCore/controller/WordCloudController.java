package com.wordCloudCore.controller;

import com.wordCloudCore.models.database_models.TextResult;
import com.wordCloudCore.repository.TextProcessProgressRepository;
import com.wordCloudCore.repository.TextResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RestController which provides endpoint for text processing result. It contains also an endpoint which will be used by
 * the WordCloudFrontend to show the user text processing progress.
 */
@RestController
public class WordCloudController {

    @Autowired
    TextResultRepository textResultRepository;
    @Autowired
    TextProcessProgressRepository textProcessProgressRepository;

    @GetMapping("/results/{text_file_id}")
    public ResponseEntity<Map<String, Long>> getResult(@PathVariable("text_file_id") String id,
                                                       @RequestHeader(value = "count_smaller_than", defaultValue = "9223372036854775807", required = false) long count_smaller_than,
                                                       @RequestHeader(value = "count_bigger_than", defaultValue = "0",required = false) long count_bigger_than){

        List<TextResult> result = textResultRepository.findByTextFileId(id, count_bigger_than, count_smaller_than);
        Map<String, Long> resultMap = new HashMap<>();

        for (TextResult t: result) {
            resultMap.put(t.getWord(), t.getCount());
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/curr_progress/{text_file_id}")
    public int getCurrentProgress(@PathVariable("text_file_id") String id){
        return textProcessProgressRepository.getCurrentProgress(id);
    }
}

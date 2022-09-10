package com.wordCloudCore.controller;

import com.wordCloudCore.models.TextProcessResult;
import com.wordCloudCore.repository.WordCloudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WordCloudController {

    @Autowired
    WordCloudRepository wordCloudRepository;

    @GetMapping("/results/{text_file_id}")
    public ResponseEntity<Map<String, Map.Entry<Long, Boolean>>> getResult(@PathVariable("text_file_id") String id){
        List<TextProcessResult> result = wordCloudRepository.findByTextFileId(id);
        Map<String, Map.Entry<Long, Boolean>> resultMap = new HashMap<>();

        for (TextProcessResult t: result) {
            resultMap.put(t.getWord(), new AbstractMap.SimpleEntry<Long, Boolean>(t.getCount(), t.isPossible_typo()));
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}

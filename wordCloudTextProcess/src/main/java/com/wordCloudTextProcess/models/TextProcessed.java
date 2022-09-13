package com.wordCloudTextProcess.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextProcessed {
    private String file_id;
    private String status;
}

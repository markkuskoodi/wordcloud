package com.wordCloudCore.models.mq_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class, that defines "textprocess_queue" message object. It contains the text file identifier and how the
 * text processing went (either passed or not).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TextProcessed {
    private String file_id;
    private String status;
}
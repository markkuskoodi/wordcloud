package com.wordCloudTextProcess.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Object which will be sent by this service to a WordCloudCore. This helps this service to let know
 * how the text processing went to the WordCloudCore.
 */
@Data
@AllArgsConstructor
public class TextProcessed {
    private String file_id;
    private String status;
}

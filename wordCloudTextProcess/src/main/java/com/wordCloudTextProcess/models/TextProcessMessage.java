package com.wordCloudTextProcess.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TextProcessMessage {
    private String messageId;
    private String file_content;
    private String omitted_words;
    private boolean possible_typos;
}

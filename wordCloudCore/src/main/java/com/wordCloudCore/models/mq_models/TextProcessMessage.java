package com.wordCloudCore.models.mq_models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.bytebuddy.utility.RandomString;

import java.util.Date;

/**
 * Class that defines the object that will be sent to the WordCloudTextProcess service. It contains text file identifier,
 * part of the text file, which words should be omitted and whether user wants to find words with spelling error or not.
 */

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

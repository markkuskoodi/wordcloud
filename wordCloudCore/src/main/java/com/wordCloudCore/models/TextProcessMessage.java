package com.wordCloudCore.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.bytebuddy.utility.RandomString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TextProcessMessage {
    private String messageId;
    private String fileName;
    private String file_content;
    private Date messageDate;
    private int message_nr;
}

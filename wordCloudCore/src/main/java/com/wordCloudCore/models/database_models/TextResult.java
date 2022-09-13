package com.wordCloudCore.models.database_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class that defines "textresult" table objects in Spring boot. It contains text file identifier, a word in text file and
 * how many these words are in the text file (count field).
 */
@Table(name="textresult")
@Entity(name="textresult")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextResult {
    @Id
    private Long id;
    private String text_file_id;
    private String word;
    private Long count;

    public TextResult(String word, Long count) {
        this.word = word;
        this.count = count;
    }
}

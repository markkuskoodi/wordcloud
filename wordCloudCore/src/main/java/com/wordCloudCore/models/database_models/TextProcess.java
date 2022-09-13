package com.wordCloudCore.models.database_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class that defines TextProcess object. This object holds a word from the text file and text file identifier.
 */

@Entity(name="textprocess")
@Table(name="textprocess")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextProcess {

    @Id
    @SequenceGenerator(name = "textprocess_id_seq",
            sequenceName = "textprocess_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "textprocess_id_seq")
    @Column(
            name = "id",
            updatable = false,
            nullable = false
    )
    private Long id;

    @Column(
            name = "word",
            columnDefinition = "VARCHAR(255)",
            nullable = false
    )
    private String word;

    @Column(
            name = "text_file_id",
            columnDefinition = "VARCHAR(255)",
            nullable = false
    )
    private String text_file_id;


    public TextProcess(String word, String text_file_id) {
        this.word = word;
        this.text_file_id = text_file_id;
    }
}

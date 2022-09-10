package com.wordCloudTextProcess.models;

import javax.persistence.*;

@Entity(name="textprocess")
@Table(name="textprocess")
public class TextProcess {

    @Id
    @SequenceGenerator(name="textprocess_id_seq", sequenceName="textprocess_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "textprocess_id_seq")
    @Column(
            name="id",
            updatable = false
    )
    private int id;

    @Column(
            name="word",
            columnDefinition = "VARCHAR(255)",
            nullable = false
    )
    private String word;

    @Column(
            name="text_file_id",
            columnDefinition = "VARCHAR(108)",
            nullable = false
    )
    private String text_file_id;

    @Column(
            name="possible_typo",
            columnDefinition = "BOOLEAN",
            nullable = false
    )
    private boolean possible_typo;

    public TextProcess(String word, String text_file_id, boolean possible_typo) {
        this.word = word;
        this.text_file_id = text_file_id;
        this.possible_typo = possible_typo;
    }

    public TextProcess() {}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getText_file_id() {
        return text_file_id;
    }

    public void setText_file_id(String text_file_id) {
        this.text_file_id = text_file_id;
    }

    public boolean isPossible_typo() {
        return possible_typo;
    }

    public void setPossible_typo(boolean possible_typo) {
        this.possible_typo = possible_typo;
    }
}

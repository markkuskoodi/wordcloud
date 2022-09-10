package com.wordCloudCore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity(name="textprocess")
@Table(name="textprocess")
public class TextProcess {

    @Id
    private int id;

    @Column(
            name="word",
            columnDefinition = "VARCHAR(255)"
    )
    private String word;

    @Column(
            name="possible_typo",
            columnDefinition = "BOOLEAN"
    )
    private boolean possible_typo;

    @ManyToOne(fetch= FetchType.LAZY, optional = false)
    @JoinColumn(name="text_file_id", nullable = false)
    @JsonIgnore
    private TextFile textFile;

    private int count;

    public String getWord() {
        return word;
    }

    public TextFile getTextFile() {
        return textFile;
    }

    public int getCount(){
        return count;
    }

    public boolean isPossible_typo() {
        return possible_typo;
    }
}

package com.wordCloudCore.models;

import lombok.Data;

@Data
public class TextProcessResult {
    private String word;
    private Long count;

    private boolean possible_typo;

    public TextProcessResult(String word, Long count, boolean possible_typo){
        this.word = word;
        this.count = count;
        this.possible_typo = possible_typo;
    }

    public String getWord() {
        return word;
    }

    public Long getCount() {
        return count;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public boolean isPossible_typo() {
        return possible_typo;
    }

    public void setPossible_typo(boolean possible_typo) {
        this.possible_typo = possible_typo;
    }
}

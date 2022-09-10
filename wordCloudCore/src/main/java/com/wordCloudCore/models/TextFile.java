package com.wordCloudCore.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "TextFile")
@Table(name = "textfile")
public class TextFile {

    @Id
    private String id;

    private String file_name;

    public TextFile(){}

    public TextFile(String id, String file_name) {
        this.id = id;
        this.file_name = file_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}

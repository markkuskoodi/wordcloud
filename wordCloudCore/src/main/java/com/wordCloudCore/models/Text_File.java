package com.wordCloudCore.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Text_file")
public class Text_File {

    @Id
    private String id;

    private String file_name;

    public Text_File(){}

    public Text_File(String id, String file_name) {
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

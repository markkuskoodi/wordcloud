package com.wordCloudCore.models.database_models;

import javax.persistence.*;
import java.time.Instant;

/**
 * This is the class which defines the text file object. This class is used to store the metadata about the text file
 * into the "textfile" table in the database. The metadata contains of text_file unique id, its original name and the metadata
 * creation date.
 */
@Table(name="textfile")
@Entity(name="textfile")
public class TextFile {

    @Id
    @Column(
            name="id",
            columnDefinition = "VARCHAR(255)",
            nullable = false
    )
    private String id;

    @Column(
            name="file_name",
            columnDefinition = "VARCHAR(255)",
            nullable = false
    )
    private String file_name;

    @Column(
            name="creation_date",
            columnDefinition = "TIMESTAMP"
    )
    private Instant creation_date;

    public TextFile(){}

    public TextFile(String id, String file_name, Instant creation_date) {
        this.id = id;
        this.file_name = file_name;
        this.creation_date = creation_date;
    }
}

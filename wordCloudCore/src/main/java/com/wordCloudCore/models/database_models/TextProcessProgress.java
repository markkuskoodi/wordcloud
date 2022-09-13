package com.wordCloudCore.models.database_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class that defines "textprocessprogress" object in Spring Boot. This object contains identifier which is related to
 * the text file, count of the messages that were sent to the microservice and the count how many of the messages are
 * finished.
 */
@Entity(name="textprocessprogress")
@Table(name="textprocessprogress")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextProcessProgress {

    @Id
    @SequenceGenerator(name = "textprocessprogress_process_id_seq",
            sequenceName = "textprocessprogress_process_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "textprocessprogress_process_id_seq")
    @Column(
            name = "process_id",
            columnDefinition = "SERIAL",
            nullable = false
    )
    private Long process_id;


    @Column(
            name = "text_file_id",
            columnDefinition = "VARCHAR(255)",
            nullable = false
    )
    private String textfileid;

    @Column(
            name = "capsuled_message_count",
            columnDefinition = "BIGINT",
            nullable = false
    )
    private long capsuled_message_count;

    @Column(
            name = "currently_processed",
            columnDefinition = "BIGINT",
            nullable = false
    )
    private long currently_processed;

    public TextProcessProgress(String text_file_id, long capsuled_message_count, long currently_processed) {
        this.textfileid = text_file_id;
        this.capsuled_message_count = capsuled_message_count;
        this.currently_processed = currently_processed;
    }
}

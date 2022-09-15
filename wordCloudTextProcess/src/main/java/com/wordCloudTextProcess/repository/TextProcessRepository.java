package com.wordCloudTextProcess.repository;

import com.wordCloudTextProcess.models.TextProcess;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository which allows this service to insert data into "textprocess" table in the database.
 */
public interface TextProcessRepository extends JpaRepository<TextProcess, Integer> {
}

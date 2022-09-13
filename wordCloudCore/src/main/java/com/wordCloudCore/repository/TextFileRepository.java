package com.wordCloudCore.repository;

import com.wordCloudCore.models.database_models.TextFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository which allows us to create queries against "textfile" table in the
 * database
 */
@Repository
public interface TextFileRepository extends JpaRepository<TextFile, String> {
}

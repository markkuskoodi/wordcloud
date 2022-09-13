package com.wordCloudCore.repository;

import com.wordCloudCore.models.database_models.TextProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * JPA repository which allows us to make queries to the "textprocess" table
 */
@Repository
@Transactional
public interface TextProcessRepository extends JpaRepository<TextProcess, Long> {

    /**
     * Query, which deletes all the rows which are related to the unique text file identifier.
     */
    @Modifying
    @Query(value = "DELETE FROM textprocess as tp WHERE tp.text_file_id=:text_file_id", nativeQuery = true)
    void deleteTextProcessFieldsByFileId(@Param("text_file_id") String text_file_id);
}

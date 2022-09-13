package com.wordCloudCore.repository;

import com.wordCloudCore.models.database_models.TextProcessProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * JPA repository which allows us to create queries against "textprocessprogress" table in the
 * database.
 */
@Repository
@Transactional
public interface TextProcessProgressRepository extends JpaRepository<TextProcessProgress, Long> {

    Optional<TextProcessProgress> findByTextfileid(String file_name);
    @Modifying
    @Query("UPDATE textprocessprogress AS tpp SET tpp.currently_processed=:currently_processed WHERE tpp.textfileid=:text_file_id")
    void setCountProgress(@Param("text_file_id") String id, @Param("currently_processed") long currently_processed);

    @Query("SELECT tpp.currently_processed FROM textprocessprogress AS tpp WHERE tpp.textfileid=:text_file_id")
    int getCurrentProgress(@Param("text_file_id") String text_file_id);
}

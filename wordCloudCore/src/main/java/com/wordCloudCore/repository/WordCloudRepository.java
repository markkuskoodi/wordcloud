package com.wordCloudCore.repository;

import com.wordCloudCore.models.TextProcess;
import com.wordCloudCore.models.TextProcessResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordCloudRepository extends JpaRepository<TextProcess, Integer> {
    @Query(value= "SELECT new com.wordCloudCore.models.TextProcessResult(tp.word, COUNT(tp.word), tp.possible_typo)" +
            " FROM textprocess as tp WHERE tp.textFile.id=:text_file_id GROUP BY tp.word, tp.possible_typo ORDER BY COUNT(tp.word) DESC")
    List<TextProcessResult> findByTextFileId(@Param("text_file_id") String text_file_id);

}

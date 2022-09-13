package com.wordCloudCore.repository;

import com.wordCloudCore.models.database_models.TextResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * TextResultRepository which allows us to make queries to the "textresult" table.
 */
@Repository
@Transactional
public interface TextResultRepository extends JpaRepository<TextResult, Long> {

    /**
     * "textresult" table query which finds the processed text file result by using its unique text_file_id. It
     * also allows to filter out word counts which are bigger or smaller than user given values.
     */
    @Query(value= "SELECT new com.wordCloudCore.models.database_models.TextResult(tr.word, tr.count)" +
            " FROM textresult as tr WHERE tr.text_file_id=:text_file_id" +
            " AND tr.count > :count_bigger_than AND tr.count < :count_smaller_than " +
            "GROUP BY tr.word, tr.count ORDER BY tr.count DESC")
    List<TextResult> findByTextFileId(@Param("text_file_id") String text_file_id, @Param("count_bigger_than") long count_bigger_than, @Param("count_smaller_than") long count_smaller_than);

    /**
     * Query, which will be run after text processing in the microservice ends. This query counts all the words in the
     * "textprocess" table and inserts them into "textresult" table.
     */
    @Modifying
    @Query(value = "INSERT INTO textresult (text_file_id, word, count) (SELECT tp.text_file_id, tp.word, COUNT(*) FROM textprocess as tp " +
            "WHERE tp.text_file_id =:text_file_id AND tp.text_file_id NOT IN (SELECT tr.text_file_id FROM textresult as tr)" +
            "GROUP BY tp.text_file_id, tp.word)", nativeQuery = true)
    void insertTextFileResults(@Param("text_file_id") String text_file_id);

}

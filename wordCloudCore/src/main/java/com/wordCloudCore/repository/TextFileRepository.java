package com.wordCloudCore.repository;

import com.wordCloudCore.models.TextFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextFileRepository extends JpaRepository<TextFile, String> {


}

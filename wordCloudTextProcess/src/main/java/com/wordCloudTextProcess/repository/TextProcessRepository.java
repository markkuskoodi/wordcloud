package com.wordCloudTextProcess.repository;

import com.wordCloudTextProcess.models.TextProcess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextProcessRepository extends JpaRepository<TextProcess, Integer> {
}

package com.wordCloudCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
public class WordCloudCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordCloudCoreApplication.class, args);
	}

}

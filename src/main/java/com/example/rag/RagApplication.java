package com.example.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.rag.config.AppProperties;
import com.example.rag.config.OpenAIProperties;

@SpringBootApplication
@EnableConfigurationProperties({OpenAIProperties.class, AppProperties.class})
public class RagApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagApplication.class, args);
	}

}

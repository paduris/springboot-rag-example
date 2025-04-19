package com.example.rag.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RagConfig {

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Value("${openai.model.chat.name}")
    private String chatModelName;

    @Value("${openai.model.chat.temperature}")
    private double chatTemperature;

    @Value("${openai.model.embedding.name}")
    private String embeddingModelName;

    @Value("${app.db.host}")
    private String dbHost;

    @Value("${app.db.port}")
    private int dbPort;

    @Value("${app.db.name}")
    private String dbName;

    @Value("${app.db.user}")
    private String dbUser;

    @Value("${app.db.password}")
    private String dbPassword;

    @Value("${app.db.table}")
    private String dbTable;

    @Value("${app.db.embedding.dimension}")
    private int embeddingDimension;

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(chatModelName)
                .temperature(chatTemperature)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(openAiApiKey)
                .modelName(embeddingModelName)
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(DataSource dataSource) {
        return PgVectorEmbeddingStore.builder()
                .host(dbHost)
                .port(dbPort)
                .database(dbName)
                .user(dbUser)
                .password(dbPassword)
                .table(dbTable)
                .dimension(embeddingDimension)
                .build();
    }
}

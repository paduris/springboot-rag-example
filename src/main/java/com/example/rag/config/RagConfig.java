package com.example.rag.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.sql.DataSource;

@Configuration
public class RagConfig {

    private final OpenAIProperties openAIProperties;
    private final AppProperties appProperties;

    public RagConfig(OpenAIProperties openAIProperties, AppProperties appProperties) {
        this.openAIProperties = openAIProperties;
        this.appProperties = appProperties;
    }

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAIProperties.getApiKey())
                .modelName(openAIProperties.getModel().getChat().getName())
                .temperature(openAIProperties.getModel().getChat().getTemperature())
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(openAIProperties.getApiKey())
                .modelName(openAIProperties.getModel().getEmbedding().getName())
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(DataSource dataSource) {
        return PgVectorEmbeddingStore.builder()
                .host(appProperties.getDb().getHost())
                .port(appProperties.getDb().getPort())
                .database(appProperties.getDb().getName())
                .user(appProperties.getDb().getUser())
                .password(appProperties.getDb().getPassword())
                .table(appProperties.getDb().getTable())
                .dimension(appProperties.getDb().getEmbedding().getDimension())
                .build();
    }
}

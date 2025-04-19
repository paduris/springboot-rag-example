package com.example.rag.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RagService {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(RagService.class.getName());

    private final ChatLanguageModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private static final int MAX_TOKENS = 300;

    public RagService(ChatLanguageModel chatModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public void processDocument(com.example.rag.model.Document document) {
        if (document == null || !StringUtils.hasText(document.getContent())) {
            log.severe("Invalid document provided for processing");
            throw new IllegalArgumentException("Document cannot be null or empty");
        }

        try {
            log.info("Processing document: " + document);
            // Split document into segments
            Document doc = Document.from(document.getContent(), Metadata.from("title", document.getTitle()));
            List<TextSegment> segments = DocumentSplitters.recursive(MAX_TOKENS, 0)
                    .split(doc);

            log.info("Document split into " + segments.size() + " segments");

            // Create and store embeddings for each segment
            for (TextSegment segment : segments) {
                Embedding embedding = embeddingModel.embed(segment.text()).content();
                embeddingStore.add(embedding, segment);
            }
            log.info("Successfully processed document with " + segments.size() + " segments");
        } catch (Exception e) {
            log.severe("Error processing document: " + e.getMessage());
            throw new RuntimeException("Failed to process document", e);
        }
    }

    public String query(String question) {
        if (!StringUtils.hasText(question)) {
            log.severe("Invalid question provided");
            throw new IllegalArgumentException("Question cannot be null or empty");
        }

        try {
            log.info("Processing query: " + question);
            
            // Create embedding for the question
            Embedding questionEmbedding = embeddingModel.embed(question).content();

            // Find relevant segments
            List<EmbeddingMatch<TextSegment>> relevantMatches = embeddingStore.findRelevant(questionEmbedding, 3);
            List<TextSegment> relevantSegments = relevantMatches.stream()
                    .map(EmbeddingMatch::embedded)
                    .toList();

            if (relevantSegments.isEmpty()) {
                log.warning("No relevant context found for question: " + question);
                return "I don't have enough context to answer this question accurately.";
            }

            // Build context from relevant segments
            String context = relevantSegments.stream()
                    .map(TextSegment::text)
                    .reduce("", (a, b) -> a + "\n" + b);

            // Generate response using chat model with context
            String prompt = String.format("""
                    Based on the following context, please answer the question. 
                    If you cannot answer the question based on the context, say so.
                    
                    Context:
                    %s
                    
                    Question: %s
                    """, context, question);

            String response = chatModel.generate(prompt);
            log.info("Generated response for question: " + question);
            return response;
            
        } catch (Exception e) {
            log.severe("Error processing query: " + e.getMessage());
            throw new RuntimeException("Failed to process query", e);
        }
    }
}

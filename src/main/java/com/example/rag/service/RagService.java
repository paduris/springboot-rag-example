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

            // Find relevant segments with higher number of matches and relevance score
            List<EmbeddingMatch<TextSegment>> relevantMatches = embeddingStore.findRelevant(
                questionEmbedding,
                5,  // Increased from 3 to 5 for more context
                0.7 // Minimum relevance score threshold
            );

            if (relevantMatches.isEmpty()) {
                log.warning("No relevant context found for question: " + question);
                return "I don't have enough context to answer this question accurately. Please try rephrasing your question or provide more specific details.";
            }

            // Build context from relevant segments with metadata
            StringBuilder contextBuilder = new StringBuilder();
            for (int i = 0; i < relevantMatches.size(); i++) {
                EmbeddingMatch<TextSegment> match = relevantMatches.get(i);
                TextSegment segment = match.embedded();
                double relevanceScore = match.score();
                contextBuilder.append("Segment ").append(i + 1)
                        .append(" (Relevance: ").append(String.format("%.2f", relevanceScore))
                        .append("):\n")
                        .append(segment.text())
                        .append("\n\n");
            }

            // Generate response using chat model with improved prompt
            String prompt = String.format(
                "You are a helpful AI assistant. Answer the question based on the provided context.\n" +
                "Be specific and detailed in your response, using information directly from the context.\n" +
                "If the context doesn't contain enough information to fully answer the question, explain what you can answer\n" +
                "and what additional information would be needed.\n\n" +
                "Context (ordered by relevance):\n%s\n" +
                "Question: %s\n\n" +
                "Instructions:\n" +
                "1. Use only the information from the context to answer the question\n" +
                "2. If the context doesn't fully answer the question, acknowledge this and explain what you can answer\n" +
                "3. Be clear and concise in your response\n" +
                "4. If you need to make assumptions, state them explicitly\n\n" +
                "Answer:\n",
                contextBuilder.toString(), question);

            String response = chatModel.generate(prompt);
            log.info("Generated response for question: " + question);
            return response;
            
        } catch (Exception e) {
            log.severe("Error processing query: " + e.getMessage());
            throw new RuntimeException("Failed to process query", e);
        }
    }
}

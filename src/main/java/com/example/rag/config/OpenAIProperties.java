package com.example.rag.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public class OpenAIProperties {
    private String apiKey;
    private ModelProperties model = new ModelProperties();

    public static class ModelProperties {
        private ChatProperties chat = new ChatProperties();
        private EmbeddingProperties embedding = new EmbeddingProperties();

        public ChatProperties getChat() {
            return chat;
        }

        public void setChat(ChatProperties chat) {
            this.chat = chat;
        }

        public EmbeddingProperties getEmbedding() {
            return embedding;
        }

        public void setEmbedding(EmbeddingProperties embedding) {
            this.embedding = embedding;
        }
    }

    public static class ChatProperties {
        private String name;
        private Double temperature;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
    }

    public static class EmbeddingProperties {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ModelProperties getModel() {
        return model;
    }

    public void setModel(ModelProperties model) {
        this.model = model;
    }
}

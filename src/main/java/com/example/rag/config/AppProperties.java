package com.example.rag.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private DatabaseProperties db = new DatabaseProperties();

    public static class DatabaseProperties {
        private String host;
        private int port;
        private String name;
        private String user;
        private String password;
        private String table;
        private EmbeddingProperties embedding = new EmbeddingProperties();

        public static class EmbeddingProperties {
            private int dimension;

            public int getDimension() {
                return dimension;
            }

            public void setDimension(int dimension) {
                this.dimension = dimension;
            }
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public EmbeddingProperties getEmbedding() {
            return embedding;
        }

        public void setEmbedding(EmbeddingProperties embedding) {
            this.embedding = embedding;
        }
    }

    public DatabaseProperties getDb() {
        return db;
    }

    public void setDb(DatabaseProperties db) {
        this.db = db;
    }
}

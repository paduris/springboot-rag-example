# Spring Boot RAG Example

This project demonstrates the implementation of Retrieval-Augmented Generation (RAG) using Spring Boot, PGVector, and OpenAI's language models.

## Prerequisites

- Java 17 or higher
- Maven
- Docker (for running PostgreSQL with pgvector)
- OpenAI API Key

## Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/springboot-rag-example.git
cd springboot-rag-example
```

2. Start PostgreSQL with pgvector using Docker:
```bash
docker-compose up -d
```

3. Configure your OpenAI API key in `application-local.properties`:
```properties
openai.api-key=your-api-key-here
```

## Configuration

The application can be configured through `application.properties`. Key configurations include:

### OpenAI Settings
- `openai.model.chat.name`: Chat model name (default: gpt-3.5-turbo)
- `openai.model.chat.temperature`: Temperature setting (default: 0.7)
- `openai.model.embedding.name`: Embedding model name (default: text-embedding-ada-002)

### Database Settings
- `app.db.host`: Database host
- `app.db.port`: Database port
- `app.db.name`: Database name
- `app.db.user`: Database user
- `app.db.password`: Database password
- `app.db.table`: Vector store table name
- `app.db.embedding.dimension`: Embedding dimension size

## Running the Application

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

## Loading PDF Documents

The application accepts PDF files through the `/api/rag/documents` endpoint. The system will:
- Validate that the uploaded file is a valid PDF
- Extract text content using Apache PDFBox
- Store metadata including:
  - Original filename
  - File size
  - MIME type
  - Page count
  - Creation timestamp
- Process the extracted text for RAG operations

## API Endpoints

### Upload PDF Document
```http
POST /api/rag/documents
Content-Type: multipart/form-data

Form Parameters:
- file: PDF file to upload
```

The endpoint will:
1. Extract text content from the PDF
2. Store document metadata (title, size, page count)
3. Generate embeddings for the content
4. Store embeddings in the vector database

### Query
```http
POST /api/rag/query
Content-Type: application/json

"Your question here"
```

## Testing

Run the tests using:
```bash
mvn test
```

The project includes:
- Unit tests for the Document model
- Controller tests with MockMvc
- Integration tests for the RAG service

## Architecture

The application follows a standard Spring Boot architecture:
- `Controller`: Handles HTTP requests
- `Service`: Contains business logic and RAG implementation
- `Model`: Data models and entities
- `Config`: Configuration classes for OpenAI and vector store setup

## Vector Store Schema

The application uses PGVector for storing document embeddings. The schema includes:
- `documents` table for storing document metadata
- `document_embeddings` table for storing vector embeddings

## Contributing

Feel free to submit issues and enhancement requests!

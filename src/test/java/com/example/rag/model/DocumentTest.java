package com.example.rag.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void testDocumentCreation() {
        Document document = new Document();
        document.setTitle("Test Document");
        document.setContent("Test Content");

        assertEquals("Test Document", document.getTitle());
        assertEquals("Test Content", document.getContent());
        assertNull(document.getId());
    }

    @Test
    void testPrePersist() {
        Document document = new Document();
        assertNull(document.getCreatedAt());
        
        document.onCreate();
        
        assertNotNull(document.getCreatedAt());
        assertTrue(document.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}

package com.example.rag.controller;

import com.example.rag.model.Document;
import com.example.rag.service.PdfService;
import com.example.rag.service.RagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RagController.class)
class RagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RagService ragService;

    @MockBean
    private PdfService pdfService;

    @Test
    void testAddDocument() throws Exception {
        // Create a mock PDF file
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "PDF content".getBytes()
        );

        Document document = new Document();
        document.setTitle("test.pdf");
        document.setContent("Extracted content");
        document.setOriginalFilename("test.pdf");
        document.setFileSize(11L);
        document.setMimeType("application/pdf");
        document.setPageCount(1);

        when(pdfService.processPdfFile(any())).thenReturn(document);
        doNothing().when(ragService).processDocument(any(Document.class));

        mockMvc.perform(multipart("/api/rag/documents")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test.pdf"))
                .andExpect(jsonPath("$.content").value("Extracted content"));

        verify(pdfService, times(1)).processPdfFile(any());
        verify(ragService, times(1)).processDocument(any(Document.class));
    }

    @Test
    void testQuery() throws Exception {
        String question = "What is RAG?";
        String expectedAnswer = "RAG is Retrieval-Augmented Generation";

        when(ragService.query(question)).thenReturn(expectedAnswer);

        mockMvc.perform(post("/api/rag/query")
                .contentType("application/json")
                .content(question))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(ragService, times(1)).query(question);
    }
}

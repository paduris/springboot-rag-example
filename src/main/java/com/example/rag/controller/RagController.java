package com.example.rag.controller;

import com.example.rag.model.Document;
import com.example.rag.service.PdfService;
import com.example.rag.service.RagService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final PdfService pdfService;

    public RagController(RagService ragService, PdfService pdfService) {
        this.ragService = ragService;
        this.pdfService = pdfService;
    }

    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Document> addDocument(@RequestParam("file") MultipartFile file) throws IOException {
        Document document = pdfService.processPdfFile(file);
        ragService.processDocument(document);
        return ResponseEntity.ok(document);
    }

    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestBody String question) {
        String answer = ragService.query(question);
        return ResponseEntity.ok(answer);
    }
}

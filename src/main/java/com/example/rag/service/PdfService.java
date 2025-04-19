package com.example.rag.service;

import com.example.rag.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class PdfService {

    public Document processPdfFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IllegalArgumentException("File must be a PDF");
        }

        try (PDDocument pdDocument = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdDocument);

            Document document = new Document();
            document.setTitle(file.getOriginalFilename());
            document.setContent(text);
            document.setOriginalFilename(file.getOriginalFilename());
            document.setFileSize(file.getSize());
            document.setMimeType(file.getContentType());
            document.setPageCount(pdDocument.getNumberOfPages());

            return document;
        }
    }
}

package com.ats.analyzer.input;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles resume file loading - extraction only.
 * Returns raw text without cleaning (cleaning happens in TextCleaner).
 */
public class ResumeLoader {

    /**
     * Load resume from file (PDF or TXT format)
     * 
     * @param filePath Path to resume file
     * @return Raw extracted text
     * @throws IOException if file cannot be read
     */
    public static String loadResume(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("Resume file not found: " + filePath);
        }

        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".pdf")) {
            return extractFromPDF(file);
        } else if (fileName.endsWith(".txt")) {
            return extractFromText(file);
        } else {
            throw new IOException("Unsupported file format. Please provide .pdf or .txt file.");
        }
    }

    /**
     * Extract text from PDF file using Apache PDFBox
     */
    private static String extractFromPDF(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    /**
     * Extract text from plain text file
     */
    private static String extractFromText(File textFile) throws IOException {
        return Files.readString(Paths.get(textFile.getPath()));
    }
}

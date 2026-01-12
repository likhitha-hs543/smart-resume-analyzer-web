package com.ats.analyzer.input;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles job description file loading.
 * Reads plain text job description files.
 */
public class JDLoader {

    /**
     * Load job description from text file
     * 
     * @param filePath Path to JD file
     * @return Raw JD text
     * @throws IOException if file cannot be read
     */
    public static String loadJD(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("Job description file not found: " + filePath);
        }

        return Files.readString(Paths.get(file.getPath()));
    }
}

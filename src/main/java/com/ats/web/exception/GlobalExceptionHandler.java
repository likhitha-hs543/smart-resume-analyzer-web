package com.ats.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for clean, consistent error responses.
 * Provides user-friendly error messages for all API endpoints.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildError("Invalid Input", ex.getMessage(), "400", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return buildError("File Too Large", "Maximum file size is 5MB. Please upload a smaller file.", "413",
                HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParameter(MissingServletRequestParameterException ex) {
        return buildError("Missing Parameter", "Required parameter '" + ex.getParameterName() + "' is missing", "400",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingPart(MissingServletRequestPartException ex) {
        return buildError("Missing File", "Resume file is required. Please select a file to upload.", "400",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedOperation(UnsupportedOperationException ex) {
        return buildError("Unsupported File Format", ex.getMessage(), "415", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return buildError("Processing Error", "An error occurred while processing your request: " + ex.getMessage(),
                "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        // Log the exception for debugging
        System.err.println("Unexpected exception: " + ex.getClass().getName());
        ex.printStackTrace();

        return buildError("Server Error", "An unexpected error occurred. Please try again later.", "500",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildError(String error, String message, String status,
            HttpStatus httpStatus) {
        Map<String, String> body = new HashMap<>();
        body.put("error", error);
        body.put("message", message);
        body.put("status", status);
        return ResponseEntity.status(httpStatus.value()).body(body);
    }
}

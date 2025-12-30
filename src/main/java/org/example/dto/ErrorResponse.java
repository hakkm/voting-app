package org.example.dto;

import java.time.LocalDateTime;

/**
 * Structured error response for API errors.
 */
public record ErrorResponse(
        int status,
        String message,
        String error,
        LocalDateTime timestamp
) {}

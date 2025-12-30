package org.example.dto;

/**
 * DTO for vote response.
 * Returned when a vote is successfully cast.
 */
public record VoteResponse(
        String voterId,
        String candidateId,
        long timestamp,
        String message
) {}

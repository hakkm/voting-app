package org.example.dto;

import java.util.Map;

/**
 * DTO for voting results response.
 */
public record ResultResponse(
        Map<String, Integer> results,
        int totalVotes,
        String strategy
) {}

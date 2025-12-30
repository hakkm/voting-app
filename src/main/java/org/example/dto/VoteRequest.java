package org.example.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for casting a new vote.
 * Includes validation constraints for voter ID and candidate selection.
 */
public record VoteRequest(
        @NotBlank(message = "Voter ID cannot be blank")
        String voterId,
        @NotBlank(message = "Candidate ID cannot be blank")
        String candidateId
) {}

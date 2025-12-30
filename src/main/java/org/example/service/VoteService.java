package org.example.service;

import org.example.exception.DuplicateVoteException;
import org.example.exception.ValidationException;
import org.example.model.Vote;
import org.example.observer.VoteListener;
import org.example.repository.VoteRepository;
import org.example.strategy.CountingStrategy;

import java.util.*;

/**
 * Service for managing votes.
 * Handles vote casting, validation, and result calculation.
 * Uses Observer pattern to notify listeners of vote events.
 */
public class VoteService {
    private final VoteRepository repo;
    private final List<VoteListener> listeners = new ArrayList<>();
    private final Set<String> voterRegistry = new HashSet<>(); // Track voters to prevent duplicates

    public VoteService(VoteRepository repo) {
        this.repo = repo;
    }

    /**
     * Add an observer to be notified of vote events.
     * @param l VoteListener implementation
     */
    public void addListener(VoteListener l) {
        listeners.add(l);
    }

    /**
     * Validate vote before casting.
     * Checks for:
     * - Non-empty voter ID
     * - Non-empty candidate ID
     * - No duplicate votes from same voter
     * @param v Vote to validate
     * @throws ValidationException if vote is invalid
     * @throws DuplicateVoteException if voter has already voted
     */
    private void validateVote(Vote v) {
        if (v.voterId() == null || v.voterId().isBlank()) {
            throw new ValidationException("Voter ID cannot be empty");
        }
        if (v.candidateId() == null || v.candidateId().isBlank()) {
            throw new ValidationException("Candidate ID cannot be empty");
        }
        if (voterRegistry.contains(v.voterId())) {
            throw new DuplicateVoteException(v.voterId());
        }
    }

    /**
     * Cast a vote and notify all observers.
     * Validates vote before casting.
     * @param v Vote to be cast
     * @throws ValidationException if vote is invalid
     * @throws DuplicateVoteException if voter has already voted
     */
    public void cast(Vote v) {
        validateVote(v);
        repo.save(v);
        voterRegistry.add(v.voterId()); // Register this voter
        // Notify all observers
        for (var l : listeners) {
            l.onVote(v);
        }
    }

    /**
     * Calculate vote results using specified counting strategy.
     * @param strategy CountingStrategy implementation (e.g., PluralityCountingStrategy)
     * @return Map of candidate to vote count
     */
    public Map<String, Integer> count(CountingStrategy strategy) {
        return strategy.count(repo.findAll());
    }

    /**
     * Get all votes cast so far.
     * @return List of all votes
     */
    public List<Vote> getAllVotes() {
        return repo.findAll();
    }

    /**
     * Clear all votes (admin operation).
     * This operation is irreversible.
     * Resets voter registry as well.
     */
    public void clearVotes() {
        repo.clear();
        voterRegistry.clear();
    }
}

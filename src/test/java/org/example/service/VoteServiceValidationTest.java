package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.exception.DuplicateVoteException;
import org.example.exception.ValidationException;
import org.example.model.Vote;
import org.example.repository.InMemoryVoteRepository;
import org.example.repository.VoteRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for VoteService validation logic.
 */
class VoteServiceValidationTest {

    private VoteService voteService;
    private VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        voteRepository = new InMemoryVoteRepository();
        voteService = new VoteService(voteRepository);
    }

    @Test
    void testCastVote_ValidVote() {
        Vote vote = new Vote("voter1", "candidate1", System.currentTimeMillis());
        assertDoesNotThrow(() -> voteService.cast(vote));
        assertEquals(1, voteService.getAllVotes().size());
    }

    @Test
    void testCastVote_NullVoterID() {
        Vote vote = new Vote(null, "candidate1", System.currentTimeMillis());
        assertThrows(ValidationException.class, () -> voteService.cast(vote),
                "Should throw ValidationException for null voter ID");
    }

    @Test
    void testCastVote_EmptyVoterID() {
        Vote vote = new Vote("", "candidate1", System.currentTimeMillis());
        assertThrows(ValidationException.class, () -> voteService.cast(vote),
                "Should throw ValidationException for empty voter ID");
    }

    @Test
    void testCastVote_NullCandidateID() {
        Vote vote = new Vote("voter1", null, System.currentTimeMillis());
        assertThrows(ValidationException.class, () -> voteService.cast(vote),
                "Should throw ValidationException for null candidate ID");
    }

    @Test
    void testCastVote_EmptyCandidateID() {
        Vote vote = new Vote("voter1", "", System.currentTimeMillis());
        assertThrows(ValidationException.class, () -> voteService.cast(vote),
                "Should throw ValidationException for empty candidate ID");
    }

    @Test
    void testCastVote_DuplicateVote() {
        Vote vote1 = new Vote("voter1", "candidate1", System.currentTimeMillis());
        Vote vote2 = new Vote("voter1", "candidate2", System.currentTimeMillis());

        // First vote should succeed
        assertDoesNotThrow(() -> voteService.cast(vote1));

        // Second vote from same voter should fail
        assertThrows(DuplicateVoteException.class, () -> voteService.cast(vote2),
                "Should throw DuplicateVoteException for second vote from same voter");
    }

    @Test
    void testCastVote_MultipleVotersOk() {
        Vote vote1 = new Vote("voter1", "candidate1", System.currentTimeMillis());
        Vote vote2 = new Vote("voter2", "candidate1", System.currentTimeMillis());
        Vote vote3 = new Vote("voter3", "candidate2", System.currentTimeMillis());

        assertDoesNotThrow(() -> {
            voteService.cast(vote1);
            voteService.cast(vote2);
            voteService.cast(vote3);
        });

        assertEquals(3, voteService.getAllVotes().size());
    }

    @Test
    void testClearVotes_ResetsVoterRegistry() {
        Vote vote = new Vote("voter1", "candidate1", System.currentTimeMillis());

        // Cast vote
        voteService.cast(vote);

        // Try to cast again - should fail
        assertThrows(DuplicateVoteException.class, () -> voteService.cast(vote));

        // Clear votes
        voteService.clearVotes();

        // Same voter should now be able to vote
        assertDoesNotThrow(() -> voteService.cast(vote));
        assertEquals(1, voteService.getAllVotes().size());
    }
}

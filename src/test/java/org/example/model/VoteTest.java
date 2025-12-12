package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VoteTest {

    @Test
    void testVoteCreation() {
        Vote vote = new Vote("voter1", "candidateA", System.currentTimeMillis());
        assertNotNull(vote);
        assertEquals("voter1", vote.voterId());
        assertEquals("candidateA", vote.candidateId());
        assertNotNull(vote.timestamp());
    }

    @Test
    void testVoteWithDifferentValues() {
        long specificTimestamp = 1678886400000L; // Example timestamp
        Vote vote = new Vote("voter2", "candidateB", specificTimestamp);
        assertNotNull(vote);
        assertEquals("voter2", vote.voterId());
        assertEquals("candidateB", vote.candidateId());
        assertEquals(specificTimestamp, vote.timestamp());
    }
}

package org.example.strategy;

import org.example.model.Vote;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BordaCountStrategyTest {

    private final BordaCountStrategy strategy = new BordaCountStrategy();

    @Test
    void testEmptyVotes() {
        List<Vote> votes = List.of();
        Map<String, Integer> result = strategy.count(votes);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleVote() {
        List<Vote> votes = List.of(
                new Vote("v1", "A", 1000)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertEquals(1, result.size());
        assertEquals(10, result.get("A"));
    }

    @Test
    void testMultipleVotes_SameCandidate() {
        List<Vote> votes = Arrays.asList(
                new Vote("v1", "A", 1000),
                new Vote("v2", "A", 1001),
                new Vote("v3", "A", 1002)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertEquals(1, result.size());
        assertEquals(30, result.get("A")); // 3 votes * 10 points each
    }

    @Test
    void testMultipleCandidates() {
        List<Vote> votes = Arrays.asList(
                new Vote("v1", "A", 1000),
                new Vote("v2", "A", 1001),
                new Vote("v3", "B", 1002),
                new Vote("v4", "C", 1003),
                new Vote("v5", "C", 1004),
                new Vote("v6", "C", 1005)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertEquals(3, result.size());
        assertEquals(20, result.get("A")); // 2 votes
        assertEquals(10, result.get("B")); // 1 vote
        assertEquals(30, result.get("C")); // 3 votes
    }
}

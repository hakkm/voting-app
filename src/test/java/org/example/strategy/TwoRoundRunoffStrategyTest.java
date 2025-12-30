package org.example.strategy;

import org.example.model.Vote;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TwoRoundRunoffStrategyTest {

    private final TwoRoundRunoffStrategy strategy = new TwoRoundRunoffStrategy();

    @Test
    void testEmptyVotes() {
        List<Vote> votes = List.of();
        Map<String, Integer> result = strategy.count(votes);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCandidateWithMajority() {
        List<Vote> votes = Arrays.asList(
                new Vote("v1", "A", 1000),
                new Vote("v2", "A", 1001),
                new Vote("v3", "A", 1002),
                new Vote("v4", "B", 1003),
                new Vote("v5", "C", 1004)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        // A has 3/5 votes (60%), which is majority
        // Should return all candidates
        assertEquals(3, result.size());
        assertEquals(3, result.get("A"));
        assertEquals(1, result.get("B"));
        assertEquals(1, result.get("C"));
    }

    @Test
    void testNoMajority_TopTwoCandidates() {
        List<Vote> votes = Arrays.asList(
                new Vote("v1", "A", 1000),
                new Vote("v2", "A", 1001),
                new Vote("v3", "B", 1002),
                new Vote("v4", "B", 1003),
                new Vote("v5", "C", 1004)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        // No candidate has majority (need >2.5 votes)
        // Should return top 2: A and B with 2 votes each
        assertEquals(2, result.size());
        assertTrue(result.containsKey("A"));
        assertTrue(result.containsKey("B"));
        assertEquals(2, result.get("A"));
        assertEquals(2, result.get("B"));
    }

    @Test
    void testExactTie_AllCandidates() {
        List<Vote> votes = Arrays.asList(
                new Vote("v1", "A", 1000),
                new Vote("v2", "B", 1001),
                new Vote("v3", "C", 1002),
                new Vote("v4", "D", 1003)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        // No majority, return top 2 (which ones depends on order)
        assertEquals(2, result.size());
    }
}

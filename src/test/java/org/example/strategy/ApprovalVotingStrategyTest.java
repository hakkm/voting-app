package org.example.strategy;

import org.example.model.Vote;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApprovalVotingStrategyTest {

    private final ApprovalVotingStrategy strategy = new ApprovalVotingStrategy();

    @Test
    void testEmptyVotes() {
        List<Vote> votes = List.of();
        Map<String, Integer> result = strategy.count(votes);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleApproval() {
        List<Vote> votes = List.of(
                new Vote("v1", "A", 1000)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertEquals(1, result.size());
        assertEquals(1, result.get("A"));
    }

    @Test
    void testMultipleApprovals() {
        List<Vote> votes = Arrays.asList(
                new Vote("v1", "A", 1000),
                new Vote("v2", "A", 1001),
                new Vote("v3", "B", 1002),
                new Vote("v4", "C", 1003),
                new Vote("v5", "A", 1004)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertEquals(3, result.size());
        assertEquals(3, result.get("A"));
        assertEquals(1, result.get("B"));
        assertEquals(1, result.get("C"));
    }

    @Test
    void testAllCandidatesApproved() {
        List<Vote> votes = Arrays.asList(
                new Vote("v1", "A", 1000),
                new Vote("v2", "B", 1001),
                new Vote("v3", "C", 1002)
        );

        Map<String, Integer> result = strategy.count(votes);
        
        assertEquals(3, result.size());
        assertEquals(1, result.get("A"));
        assertEquals(1, result.get("B"));
        assertEquals(1, result.get("C"));
    }
}

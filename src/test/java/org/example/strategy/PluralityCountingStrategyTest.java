package org.example.strategy;

import org.example.model.Vote;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluralityCountingStrategyTest {

    @Test
    void testCountWithEmptyVotes() {
        PluralityCountingStrategy strategy = new PluralityCountingStrategy();
        Map<String, Integer> results = strategy.count(List.of());
        assertTrue(results.isEmpty());
    }

    @Test
    void testCountWithSingleVote() {
        PluralityCountingStrategy strategy = new PluralityCountingStrategy();
        Vote vote = new Vote("voter1", "candidateA", System.currentTimeMillis());
        Map<String, Integer> results = strategy.count(List.of(vote));
        assertEquals(1, results.size());
        assertEquals(1, results.get("candidateA"));
    }

    @Test
    void testCountWithMultipleVotesForSameCandidate() {
        PluralityCountingStrategy strategy = new PluralityCountingStrategy();
        Vote vote1 = new Vote("voter1", "candidateA", System.currentTimeMillis());
        Vote vote2 = new Vote("voter2", "candidateA", System.currentTimeMillis());
        Map<String, Integer> results = strategy.count(List.of(vote1, vote2));
        assertEquals(1, results.size());
        assertEquals(2, results.get("candidateA"));
    }

    @Test
    void testCountWithMultipleVotesForDifferentCandidates() {
        PluralityCountingStrategy strategy = new PluralityCountingStrategy();
        Vote vote1 = new Vote("voter1", "candidateA", System.currentTimeMillis());
        Vote vote2 = new Vote("voter2", "candidateB", System.currentTimeMillis() + 1);
        Vote vote3 = new Vote("voter3", "candidateA", System.currentTimeMillis() + 2);
        Map<String, Integer> results = strategy.count(List.of(vote1, vote2, vote3));
        assertEquals(2, results.size());
        assertEquals(2, results.get("candidateA"));
        assertEquals(1, results.get("candidateB"));
    }

    @Test
    void testCountWithMixedVotesAndNulls() {
        PluralityCountingStrategy strategy = new PluralityCountingStrategy();
        Vote vote1 = new Vote("voter1", "candidateA", System.currentTimeMillis());
        Vote vote2 = new Vote("voter2", "candidateB", System.currentTimeMillis() + 1);
        Vote vote3 = new Vote("voter3", "candidateA", System.currentTimeMillis() + 2);
        Vote vote4 = new Vote("voter4", null, System.currentTimeMillis() + 3); // Test with null candidateId
        Map<String, Integer> results = strategy.count(List.of(vote1, vote2, vote3, vote4));
        assertEquals(3, results.size());
        assertEquals(2, results.get("candidateA"));
        assertEquals(1, results.get("candidateB"));
        assertEquals(1, results.get(null));
    }
}

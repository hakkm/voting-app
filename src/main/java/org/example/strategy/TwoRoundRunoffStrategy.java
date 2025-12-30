package org.example.strategy;

import org.example.model.Vote;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Two-Round Runoff voting system.
 * 
 * In the first round, if no candidate receives a majority (>50%), 
 * the top two candidates proceed to a runoff.
 * This implementation simulates the first round results.
 * 
 * Returns:
 * - All candidates if someone has majority
 * - Top 2 candidates if no majority (indicating runoff needed)
 */
public class TwoRoundRunoffStrategy implements CountingStrategy {
    
    @Override
    public Map<String, Integer> count(List<Vote> votes) {
        if (votes.isEmpty()) {
            return new HashMap<>();
        }

        // Count votes for each candidate
        Map<String, Integer> results = new HashMap<>();
        for (Vote vote : votes) {
            results.merge(vote.candidateId(), 1, Integer::sum);
        }

        int totalVotes = votes.size();
        int majorityThreshold = totalVotes / 2;

        // Check if any candidate has majority
        boolean hasMajority = results.values().stream()
                .anyMatch(count -> count > majorityThreshold);

        if (hasMajority) {
            return results;
        }

        // No majority - return top 2 candidates for runoff
        return results.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(2)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}

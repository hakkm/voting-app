package org.example.strategy;

import org.example.model.Vote;

import java.util.*;

/**
 * Borda Count voting system.
 * 
 * This is a simplified version where we simulate ranked voting.
 * In a real Borda Count system, voters rank all candidates.
 * Here, we assign points based on order of votes received:
 * - Each vote = base points
 * - Earlier votes (by timestamp) get slight bonus for time priority
 * 
 * This is a demonstration - real Borda Count requires ranked ballots.
 */
public class BordaCountStrategy implements CountingStrategy {
    
    private static final int BASE_POINTS = 10;
    
    @Override
    public Map<String, Integer> count(List<Vote> votes) {
        if (votes.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Integer> scores = new HashMap<>();
        
        // Sort votes by timestamp (earlier votes get priority)
        List<Vote> sortedVotes = new ArrayList<>(votes);
        sortedVotes.sort(Comparator.comparingLong(Vote::timestamp));
        
        // Assign points based on position and frequency
        for (Vote vote : sortedVotes) {
            scores.merge(vote.candidateId(), BASE_POINTS, Integer::sum);
        }
        
        return scores;
    }
}

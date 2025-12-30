package org.example.strategy;

import org.example.model.Vote;

import java.util.*;

/**
 * Approval Voting system.
 * 
 * In true approval voting, each voter can approve multiple candidates.
 * Since our Vote model supports one candidate per vote, this implementation
 * treats each vote as an approval.
 * 
 * This is functionally equivalent to Plurality for single-choice votes,
 * but demonstrates the pattern for future enhancement where voters
 * could submit multiple approvals.
 */
public class ApprovalVotingStrategy implements CountingStrategy {
    
    @Override
    public Map<String, Integer> count(List<Vote> votes) {
        Map<String, Integer> approvals = new HashMap<>();
        
        for (Vote vote : votes) {
            approvals.merge(vote.candidateId(), 1, Integer::sum);
        }
        
        return approvals;
    }
}

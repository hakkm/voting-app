package org.example.observer;


import org.example.model.Vote;

public class LoggingVoteListener implements VoteListener {
    @Override
    public void onVote(Vote vote) {
        System.out.println("[LOG] A vote was cast: " + vote);
    }
}

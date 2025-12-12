package org.example.observer;


import org.example.model.Vote;

public interface VoteListener {
    void onVote(Vote vote);
}

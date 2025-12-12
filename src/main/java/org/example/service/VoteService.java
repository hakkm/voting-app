package org.example.service;

import org.example.model.Vote;
import org.example.observer.VoteListener;
import org.example.repository.VoteRepository;
import org.example.strategy.CountingStrategy;

import java.util.*;

public class VoteService {
    private final VoteRepository repo;
    private final List<VoteListener> listeners = new ArrayList<>();

    public VoteService(VoteRepository repo) {
        this.repo = repo;
    }

    public void addListener(VoteListener l) {
        listeners.add(l);
    }

    public void cast(Vote v) {
        repo.save(v);
        // Notify all observers
        for (var l : listeners) {
            l.onVote(v);
        }
    }

    public Map<String, Integer> count(CountingStrategy strategy) {
        return strategy.count(repo.findAll());
    }
}

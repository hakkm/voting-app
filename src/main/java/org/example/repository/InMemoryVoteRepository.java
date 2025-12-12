package org.example.vote.repo;

import org.example.model.Vote;
import org.example.repository.VoteRepository;
import java.util.*;

public class InMemoryVoteRepository implements VoteRepository {
    private final List<Vote> store = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void save(Vote vote) {
        store.add(vote);
    }

    @Override
    public List<Vote> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public void clear() {
        store.clear();
    }
}

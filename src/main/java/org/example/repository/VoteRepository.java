package org.example.repository;

import org.example.model.Vote;

import java.util.List;

public interface VoteRepository {
    void save(Vote vote);
    List<Vote> findAll();
    void clear();
}

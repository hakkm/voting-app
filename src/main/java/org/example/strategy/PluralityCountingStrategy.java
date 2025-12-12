package org.example.strategy;

import org.example.model.Vote;

import java.util.*;

public class PluralityCountingStrategy implements CountingStrategy {
    @Override
    public Map<String, Integer> count(List<Vote> votes) {
        Map<String, Integer> results = new HashMap<>();
        for (var v : votes) {
            results.merge(v.candidateId(), 1, Integer::sum);
        }
        return results;
    }
}

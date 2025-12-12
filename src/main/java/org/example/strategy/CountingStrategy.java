package org.example.strategy;


import org.example.model.Vote;

import java.util.List;
import java.util.Map;

public interface CountingStrategy {
    Map<String, Integer> count(List<Vote> votes);
}

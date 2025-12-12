package org.example.service;

import org.example.model.Vote;
import org.example.observer.VoteListener;
import org.example.repository.VoteRepository;
import org.example.strategy.CountingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteListener voteListener;
    @Mock
    private CountingStrategy countingStrategy;

    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        voteService.addListener(voteListener);
    }

    @Test
    void addListener() {
        VoteListener anotherVoteListener = mock(VoteListener.class);
        voteService.addListener(anotherVoteListener);
        Vote vote = new Vote("voter1", "Alice", 1L);
        voteService.cast(vote);
        verify(voteRepository, times(1)).save(vote);
        verify(anotherVoteListener, times(1)).onVote(vote);
    }

    @Test
    void cast() {
        Vote vote = new Vote("voter1", "Alice", 1L);
        voteService.cast(vote);
        verify(voteRepository, times(1)).save(vote);
        verify(voteListener, times(1)).onVote(vote);
    }

    @Test
    void count() {
        Vote vote1 = new Vote("voter1", "Alice", 1L);
        Vote vote2 = new Vote("voter2", "Bob", 2L);
        List<Vote> allVotes = Arrays.asList(vote1, vote2);
        Map<String, Integer> expectedCounts = Map.of("Alice", 1, "Bob", 1);

        when(voteRepository.findAll()).thenReturn(allVotes);
        when(countingStrategy.count(allVotes)).thenReturn(expectedCounts);

        Map<String, Integer> actualCounts = voteService.count(countingStrategy);

        assertEquals(expectedCounts, actualCounts);
        verify(voteRepository, times(1)).findAll();
        verify(countingStrategy, times(1)).count(allVotes);
    }
}
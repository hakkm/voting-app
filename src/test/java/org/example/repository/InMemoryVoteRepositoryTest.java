package org.example.repository;

import org.example.model.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryVoteRepositoryTest {

    private InMemoryVoteRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVoteRepository();
    }

    @Test
    void testSaveAndFindAll() {
        assertTrue(repository.findAll().isEmpty());

        Vote vote1 = new Vote("voter1", "candidateA", System.currentTimeMillis());
        repository.save(vote1);
        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findAll().contains(vote1));

        Vote vote2 = new Vote("voter2", "candidateB", System.currentTimeMillis() + 1);
        repository.save(vote2);
        assertEquals(2, repository.findAll().size());
        assertTrue(repository.findAll().contains(vote1));
        assertTrue(repository.findAll().contains(vote2));
    }

    @Test
    void testClear() {
        Vote vote1 = new Vote("voter1", "candidateA", System.currentTimeMillis());
        repository.save(vote1);
        assertFalse(repository.findAll().isEmpty());

        repository.clear();
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testFindAllReturnsCopy() {
        Vote vote1 = new Vote("voter1", "candidateA", System.currentTimeMillis());
        repository.save(vote1);

        List<Vote> allVotes = repository.findAll();
        assertEquals(1, allVotes.size());

        // Modify the returned list, check if the internal store is unaffected
        allVotes.clear();
        assertEquals(1, repository.findAll().size());
    }
}

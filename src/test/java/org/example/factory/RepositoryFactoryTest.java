package org.example.factory;

import org.example.repository.VoteRepository;
import org.example.vote.repo.InMemoryVoteRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class RepositoryFactoryTest {

    @Test
    void testCreateInMemoryRepository() {
        VoteRepository repository = RepositoryFactory.createRepo("memory");
        assertInstanceOf(InMemoryVoteRepository.class, repository);
    }

    @Test
    void testCreateInMemoryRepositoryCaseInsensitive() {
        VoteRepository repository = RepositoryFactory.createRepo("Memory");
        assertInstanceOf(InMemoryVoteRepository.class, repository);

        repository = RepositoryFactory.createRepo("MEMORY");
        assertInstanceOf(InMemoryVoteRepository.class, repository);
    }

    @Test
    void testCreateUnknownRepositoryTypeThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            RepositoryFactory.createRepo("unknown");
        });
        assertTrue(thrown.getMessage().contains("Unknown repo type: unknown"));
    }
}

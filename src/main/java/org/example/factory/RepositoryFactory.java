/*
 * Factory class to create VoteRepository instances based on type.
 * Currently supports only in-memory repository.
 */
package org.example.factory;


import org.example.repository.VoteRepository;


public class RepositoryFactory {
    public static VoteRepository createRepo(String type) {
        if ("memory".equalsIgnoreCase(type)) {
            return new org.example.vote.repo.InMemoryVoteRepository();
        }
        throw new IllegalArgumentException("Unknown repo type: " + type);
    }
}

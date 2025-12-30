package org.example.exception;

/**
 * Exception thrown when duplicate vote is detected.
 */
public class DuplicateVoteException extends ValidationException {
    public DuplicateVoteException(String voterId) {
        super("Voter " + voterId + " has already cast a vote");
    }
}

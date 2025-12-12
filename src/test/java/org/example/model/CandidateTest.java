package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CandidateTest {

    @Test
    void testCandidateCreation() {
        Candidate candidate = new Candidate("1", "John Doe");
        assertNotNull(candidate);
        assertEquals("1", candidate.id());
        assertEquals("John Doe", candidate.name());
    }

    @Test
    void testCandidateWithDifferentValues() {
        Candidate candidate = new Candidate("2", "Jane Smith");
        assertNotNull(candidate);
        assertEquals("2", candidate.id());
        assertEquals("Jane Smith", candidate.name());
    }
}

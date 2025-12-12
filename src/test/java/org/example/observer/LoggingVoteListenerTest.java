package org.example.observer;

import org.example.model.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggingVoteListenerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testOnVoteLogsCorrectly() {
        LoggingVoteListener listener = new LoggingVoteListener();
        Vote vote = new Vote("voter1", "candidateA", System.currentTimeMillis());
        listener.onVote(vote);
        String expectedLog = "[LOG] A vote was cast: " + vote.toString();
        assertTrue(outContent.toString().contains(expectedLog));
    }
}

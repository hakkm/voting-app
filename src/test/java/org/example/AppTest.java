package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testAppVoteAndCount() {
        String input = "vote\n" +
                       "voter1\n" +
                       "candidateA\n" +
                       "vote\n" +
                       "voter2\n" +
                       "candidateA\n" +
                       "vote\n" +
                       "voter3\n" +
                       "candidateB\n" +
                       "count\n" +
                       "exit\n";
        provideInput(input);

        App.main(new String[]{});

        String output = outContent.toString();
        // Initial setup messages
        assertTrue(output.contains("--- Refactored Voting App ---"));
        assertTrue(output.contains("Commands: vote, count, exit"));

        // Check for vote recorded messages (3 times for 3 votes)
        long voteRecordedCount = output.lines().filter(line -> line.contains("Vote recorded.")).count();
        org.junit.jupiter.api.Assertions.assertEquals(3L, voteRecordedCount, "Expected 3 'Vote recorded.' messages.");

        System.err.println("Actual output: \n" + output); // Debug print
        assertTrue(output.contains("Results: {"));
        assertTrue(output.contains("candidateA=2"));
        assertTrue(output.contains("candidateB=1"));
        assertTrue(output.contains("}"));

        // Check for logging messages (3 times for 3 votes)
        long logCount = output.lines().filter(line -> line.contains("[LOG] A vote was cast:")).count();
        org.junit.jupiter.api.Assertions.assertEquals(3L, logCount, "Expected 3 '[LOG] A vote was cast:' messages.");
    }

    @Test
    void testAppUnknownCommand() {
        String input = "unknown\n" +
                       "exit\n";
        provideInput(input);

        App.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Unknown command. Try harder."));
    }

    @Test
    void testAppExitCommand() {
        String input = "exit\n";
        provideInput(input);

        App.main(new String[]{});

        String output = outContent.toString();
        // Check if the application exited cleanly without further output after 'exit'
        // This implicitly means the loop was broken.
        assertTrue(outContent.toString().trim().endsWith(">"));
    }
}

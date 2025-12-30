package org.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.VoteRequest;
import org.example.service.VoteService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Integration tests for VoteController REST endpoints.
 * Tests the full Spring application context including service and repository.
 */
@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        // Clear votes before each test
        voteService.clearVotes();
    }

    @Test
    void testCastVote_Success() throws Exception {
        VoteRequest request = new VoteRequest("voter1", "candidate1");

        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.voterId").value("voter1"))
                .andExpect(jsonPath("$.candidateId").value("candidate1"))
                .andExpect(jsonPath("$.message").value("Vote successfully recorded"));
    }

    @Test
    void testCastVote_InvalidVoterID() throws Exception {
        VoteRequest request = new VoteRequest("", "candidate1");

        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").value(containsString("Voter ID cannot be blank")));
    }

    @Test
    void testCastVote_InvalidCandidateID() throws Exception {
        VoteRequest request = new VoteRequest("voter1", "");

        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").value(containsString("Candidate ID cannot be blank")));
    }

    @Test
    void testCastVote_DuplicateVote() throws Exception {
        VoteRequest request = new VoteRequest("voter1", "candidate1");

        // Cast first vote - should succeed
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Cast second vote from same voter - should fail
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").value(containsString("already cast a vote")));
    }

    @Test
    void testGetResults_EmptyRepository() throws Exception {
        mockMvc.perform(get("/api/votes/results")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isMap())
                .andExpect(jsonPath("$.totalVotes").value(0))
                .andExpect(jsonPath("$.strategy").value("plurality"));
    }

    @Test
    void testGetResults_WithVotes() throws Exception {
        // Cast multiple votes
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VoteRequest("voter1", "candidateA"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VoteRequest("voter2", "candidateA"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VoteRequest("voter3", "candidateB"))))
                .andExpect(status().isCreated());

        // Get results
        mockMvc.perform(get("/api/votes/results")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVotes").value(3))
                .andExpect(jsonPath("$.results.candidateA").value(2))
                .andExpect(jsonPath("$.results.candidateB").value(1));
    }

    @Test
    void testGetAllVotes_Empty() throws Exception {
        mockMvc.perform(get("/api/votes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetAllVotes_WithVotes() throws Exception {
        // Cast some votes
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VoteRequest("voter1", "candidate1"))))
                .andExpect(status().isCreated());

        // Get all votes
        mockMvc.perform(get("/api/votes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].voterId").value("voter1"))
                .andExpect(jsonPath("$[0].candidateId").value("candidate1"));
    }

    @Test
    void testClearVotes() throws Exception {
        // Cast some votes
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VoteRequest("voter1", "candidate1"))))
                .andExpect(status().isCreated());

        // Verify votes exist
        mockMvc.perform(get("/api/votes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Clear votes
        mockMvc.perform(delete("/api/votes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("All votes have been cleared"));

        // Verify votes are cleared
        mockMvc.perform(get("/api/votes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testClearVotes_AllowsRevoterAfterClear() throws Exception {
        VoteRequest request = new VoteRequest("voter1", "candidate1");

        // Cast first vote
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Duplicate vote should fail
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Clear votes
        mockMvc.perform(delete("/api/votes"))
                .andExpect(status().isOk());

        // Same voter should now be able to vote again
        mockMvc.perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}

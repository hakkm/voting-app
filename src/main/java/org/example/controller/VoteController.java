package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultResponse;
import org.example.dto.VoteRequest;
import org.example.dto.VoteResponse;
import org.example.model.Vote;
import org.example.service.VoteService;
import org.example.strategy.CountingStrategy;
import org.example.strategy.PluralityCountingStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for voting operations.
 * Provides endpoints for casting votes and retrieving results.
 */
@Slf4j
@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /**
     * Cast a new vote.
     * @param request VoteRequest containing voterId and candidateId
     * @return VoteResponse with vote confirmation
     */
    @PostMapping
    public ResponseEntity<VoteResponse> castVote(@Valid @RequestBody VoteRequest request) {
        log.info("Casting vote for voter: {}, candidate: {}", request.voterId(), request.candidateId());

        Vote vote = new Vote(request.voterId(), request.candidateId(), System.currentTimeMillis());
        voteService.cast(vote);

        VoteResponse response = new VoteResponse(
                vote.voterId(),
                vote.candidateId(),
                vote.timestamp(),
                "Vote successfully recorded"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get voting results using default Plurality counting strategy.
     * @param strategy Optional strategy parameter: plurality (default), runoff, borda, approval
     * @return ResultResponse with vote tallies
     */
    @GetMapping("/results")
    public ResponseEntity<ResultResponse> getResults(
            @RequestParam(defaultValue = "plurality") String strategy) {
        log.info("Retrieving voting results with strategy: {}", strategy);

        CountingStrategy countingStrategy = getCountingStrategy(strategy);
        Map<String, Integer> results = voteService.count(countingStrategy);
        List<Vote> allVotes = voteService.getAllVotes();

        ResultResponse response = new ResultResponse(
                results,
                allVotes.size(),
                strategy
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Select counting strategy based on parameter.
     */
    private CountingStrategy getCountingStrategy(String strategy) {
        return switch (strategy.toLowerCase()) {
            case "runoff" -> new org.example.strategy.TwoRoundRunoffStrategy();
            case "borda" -> new org.example.strategy.BordaCountStrategy();
            case "approval" -> new org.example.strategy.ApprovalVotingStrategy();
            default -> new PluralityCountingStrategy();
        };
    }

    /**
     * Get all votes (admin endpoint).
     * @return List of all votes cast
     */
    @GetMapping
    public ResponseEntity<List<Vote>> getAllVotes() {
        log.info("Retrieving all votes");
        List<Vote> allVotes = voteService.getAllVotes();
        return ResponseEntity.ok(allVotes);
    }

    /**
     * Clear all votes (admin endpoint).
     * WARNING: This operation is irreversible.
     * @return Confirmation message
     */
    @DeleteMapping
    public ResponseEntity<String> clearVotes() {
        log.warn("Clearing all votes - admin operation");
        voteService.clearVotes();
        return ResponseEntity.ok("All votes have been cleared");
    }
}

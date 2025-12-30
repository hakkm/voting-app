package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.example.factory.RepositoryFactory;
import org.example.observer.LoggingVoteListener;
import org.example.repository.VoteRepository;
import org.example.service.VoteService;

/**
 * Spring Boot Application for Voting System.
 * 
 * This application provides both:
 * - REST API for vote management (http://localhost:8080/api/votes)
 * - Swagger UI documentation (http://localhost:8080/swagger-ui.html)
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * Create VoteRepository bean using factory pattern.
     * Configuration can be externalized via application.properties
     */
    @Bean
    public VoteRepository voteRepository() {
        return RepositoryFactory.createRepo("memory");
    }

    /**
     * Create VoteService bean with repository dependency injection.
     */
    @Bean
    public VoteService voteService(VoteRepository repo) {
        VoteService service = new VoteService(repo);
        // Add logging listener for vote events
        service.addListener(new LoggingVoteListener());
        return service;
    }
}


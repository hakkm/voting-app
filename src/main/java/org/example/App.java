package org.example;


import org.example.factory.RepositoryFactory;
import org.example.model.Vote;
import org.example.observer.LoggingVoteListener;
import org.example.repository.VoteRepository;
import org.example.service.VoteService;
import org.example.strategy.PluralityCountingStrategy;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // 1. Setup via Factory
        VoteRepository repo = RepositoryFactory.createRepo("memory");

        // 2. Init Service and Observers
        VoteService service = new VoteService(repo);
        service.addListener(new LoggingVoteListener());

        Scanner sc = new Scanner(System.in);
        System.out.println("--- Refactored Voting App ---");
        System.out.println("Commands: vote, count, exit");

        while (true) {
            System.out.print("> ");
            String cmd = sc.nextLine().trim();

            if ("exit".equalsIgnoreCase(cmd)) break;

            if ("vote".equalsIgnoreCase(cmd)) {
                System.out.print("Voter Name: ");
                String voter = sc.nextLine();
                System.out.print("Candidate Name: ");
                String candidate = sc.nextLine();

                Vote v = new Vote(voter, candidate, System.currentTimeMillis());
                service.cast(v);
                System.out.println("Vote recorded.");
            }
            else if ("count".equalsIgnoreCase(cmd)) {
                var results = service.count(new PluralityCountingStrategy());
                System.out.println("Results: " + results);
            }
            else {
                System.out.println("Unknown command. Try harder.");
            }
        }
    }
}

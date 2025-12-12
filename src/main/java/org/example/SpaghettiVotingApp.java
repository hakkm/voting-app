package org.example;

import java.util.*;

public class SpaghettiVotingApp {
    static List<String> candidates = new ArrayList<>();
    static List<String> voters = new ArrayList<>();
    static Map<String,Integer> votes = new HashMap<>();

    public static void main(String[] args) {
        init();
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to VotingApp! Type 'vote' to vote, 'count' to count, 'exit' to quit");
        while(true) {
            String cmd = sc.nextLine();
            if(cmd.equals("vote")) {
                System.out.println("Enter voter name:");
                String v = sc.nextLine();
                if(!voters.contains(v)) voters.add(v);
                System.out.println("Candidates: " + candidates);
                System.out.println("Enter candidate:");
                String c = sc.nextLine();
                if(!candidates.contains(c)) {
                    candidates.add(c);
                    votes.put(c,0);
                }
                // duplicate checks not good, concurrency ignored
                int old = votes.containsKey(c)?votes.get(c):0;
                votes.put(c, old+1);
                System.out.println("Thanks " + v + " for voting to " + c);
            } else if(cmd.equals("count")) {
                // messy: both print and compute here
                System.out.println("Counting votes...");
                int total=0;
                for(String k: votes.keySet()) total+=votes.get(k);
                System.out.println("Total votes: "+ total);
                String winner=null; int max=-1;
                for(String k: votes.keySet()) {
                    System.out.println("Candidate " + k + " => " + votes.get(k));
                    if(votes.get(k) > max) {
                        max = votes.get(k);
                        winner = k;
                    }
                }
                System.out.println("Winner is: " + winner);
            } else if(cmd.equals("addcandidate")) {
                System.out.println("Enter candidate name:");
                String c = sc.nextLine();
                candidates.add(c);
                votes.put(c,0);
            } else if(cmd.equals("reset")) {
                candidates.clear(); voters.clear(); votes.clear();
                init();
                System.out.println("Reset done");
            } else if(cmd.equals("exit")) {
                System.out.println("Bye!");
                break;
            } else if(cmd.equals("dump")) {
                // debug method dumps all state
                System.out.println("CANDIDATES:" + candidates);
                System.out.println("VOTERS:" + voters);
                System.out.println("VOTES:" + votes);
            } else {
                System.out.println("Unknown command");
            }
        }
        sc.close();
    }

    static void init(){
        candidates.add("Alice");
        candidates.add("Bob");
        votes.put("Alice", 0);
        votes.put("Bob", 0);
    }
}

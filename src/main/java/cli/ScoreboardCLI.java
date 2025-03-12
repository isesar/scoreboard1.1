package cli;

import enums.Team;
import model.Match;
import org.springframework.boot.CommandLineRunner;
import service.ScoreboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ScoreboardCLI implements CommandLineRunner {

    private final ScoreboardService service;
    private final Scanner scanner;
   private static final Logger logger = LoggerFactory.getLogger(ScoreboardCLI.class);

    public ScoreboardCLI(ScoreboardService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) {

      logger.info("Application starting...");

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    startMatch();
                    break;
                case 2:
                    updateScore();
                    break;
                case 3:
                    finishMatch();
                    break;
                case 4:
                    displayLiveSummary();
                    break;
                case 5:
                    displayAllSummary();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nLive Football World Cup Scoreboard");
        System.out.println("1. Start Match");
        System.out.println("2. Update Score");
        System.out.println("3. Finish Match");
        System.out.println("4. Display Live Scores");
        System.out.println("5. Display All Scores");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private void startMatch() {
        logger.info("Starting match...");

        List<Team> availableTeams = getAvailableTeams();
        System.out.println("\nAvailable teams:");
        printTeams(availableTeams);

        Team homeTeam = selectTeam("home", availableTeams);
        availableTeams.remove(homeTeam);

        printTeams(availableTeams);

        Team awayTeam = selectTeam("away", availableTeams);
        availableTeams.remove(awayTeam);

        service.startMatch(homeTeam, awayTeam);
        System.out.println("Match started: " + homeTeam.getDisplayName() + " vs " + awayTeam.getDisplayName());

        logger.info("Match started.");
    }

    private Team selectTeam(String teamType, List<Team> availableTeams) {
        logger.info("Selecting {} team...", teamType);
        while (true) {
            System.out.print("Enter " + teamType + " team number: ");
            int teamIndex = scanner.nextInt();
            if (teamIndex >= 0 && teamIndex < availableTeams.size()) {
                return availableTeams.get(teamIndex);
            }
            System.out.println("Invalid team number. Please try again.");
        }
    }

    private List<Team> getAvailableTeams() {
        List<Team> allTeams = new ArrayList<>(Arrays.asList(Team.values()));
        List<Match> liveMatches = service.getLiveSummary();
        for (Match match : liveMatches) {
            allTeams.remove(match.getHomeTeam());
            allTeams.remove(match.getAwayTeam());
        }
        return allTeams;
    }


    private void updateScore() {
        logger.info("Updating score...");
        List<Match> liveMatches = service.getLiveSummary();
        if (liveMatches.isEmpty()) {
            System.out.println("No live matches to update.");
            return;
        }

        System.out.println("Select a match to update:");
        printMatches(liveMatches);

        int matchIndex = scanner.nextInt();
        if (matchIndex < 0 || matchIndex >= liveMatches.size()) {
            System.out.println("Invalid match selection.");
            return;
        }

        Match selectedMatch = liveMatches.get(matchIndex);
        System.out.print("Enter new home team score: ");
        int homeScore = scanner.nextInt();
        System.out.print("Enter new away team score: ");
        int awayScore = scanner.nextInt();

        service.updateScore(selectedMatch.getHomeTeam().name(), selectedMatch.getAwayTeam().name(), homeScore, awayScore);
        logger.info("Score updated.");
    }

    private void finishMatch() {
        logger.info("Finishing match...");
        List<Match> liveMatches = service.getLiveSummary();
        if (liveMatches.isEmpty()) {
            System.out.println("No live matches to finish.");
            return;
        }

        System.out.println("Select a match to finish:");
        printMatches(liveMatches);


        int matchIndex = scanner.nextInt();
        if (matchIndex < 0 || matchIndex >= liveMatches.size()) {
            System.out.println("Invalid match selection.");
            return;
        }

        Match selectedMatch = liveMatches.get(matchIndex);
        service.finishMatch(selectedMatch.getHomeTeam().name(), selectedMatch.getAwayTeam().name());
        logger.info("Match finished and removed from scoreboard.");
    }

    private void displayLiveSummary() {
        List<Match> summary = service.getLiveSummary();
        if (summary.isEmpty()) {
            System.out.println("No matches in progress.");
        } else {
            System.out.println("\nLive Scoreboard:");
            for (int i = 0; i < summary.size(); i++) {
                System.out.println((i + 1) + ". " + summary.get(i));
            }
        }
    }

    private void displayAllSummary() {
        List<Match> summary = service.getAllMatches();
        if (summary.isEmpty()) {
            System.out.println("No matches in progress.");
        } else {
            System.out.println("\nComplete Scoreboard:");
            for (int i = 0; i < summary.size(); i++) {
                System.out.println((i + 1) + ". " + summary.get(i));
            }
        }
    }

    private void printMatches(List<Match> matches){
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(i + ". " + matches.get(i));
        }
    }

    private void printTeams(List<Team> teams){
        for (int i = 0; i < teams.size(); i++) {
            System.out.println(i + ". " + teams.get(i).getDisplayName());
        }
    }
}

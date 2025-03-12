package cli;

import enums.Team;
import models.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.ScoreboardService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreboardCLITest {
    private ScoreboardCLI cli;
    @Mock
    private ScoreboardService service;
    @Mock
    private Scanner scanner;

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cli = new ScoreboardCLI(service, scanner);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testStartMatchOperation() {
        // Arrange: simulate user input for menu option and team selection
        when(scanner.nextInt())
                .thenReturn(1) // User selects "Start Match" operation (key 1 in the menu)
                .thenReturn(0) // Select the first team (index 0)
                .thenReturn(0) // Select the second team (index 1)
                .thenReturn(6);
//                .thenReturn(scanner.nextInt());

        when(scanner.nextLine()).thenReturn("\n"); // To consume the newline after integer inputs

        // Mock available teams and define service behavior
        List<Team> availableTeams = new ArrayList<>(List.of(Team.values()));
        when(service.getLiveSummary()).thenReturn(new ArrayList<>()); // No matches initially

        // Act: Run the CLI
        cli.run();

        // Assert: Verify behavior and output
        verify(service).startMatch(availableTeams.get(0), availableTeams.get(1));
        System.out.println("here" +availableTeams.get(0) + availableTeams.get(1));

        System.out.println("Captured Output: " + outContent.toString().trim()+ availableTeams.get(0) + availableTeams.get(0));
        assertTrue(outContent.toString().trim().contains("Match started: Mexico vs Canada" ));

    }

    @Test
    public void testUpdateMatchOperation() {
        // Arrange: simulate user input for menu option and team selection
// Arrange: Create a match and mock service behavior
        Team team1 = Team.CANADA;
        Team team2 = Team.MEXICO;
        Match match = new Match(team1, team2); // Initial score (0:0)

        // Mock service output for match list
        when(service.getLiveSummary()).thenReturn(List.of(match));
        // Mock behavior for updating a match
        doNothing().when(service).updateScore(match.getHomeTeam().name(), match.getAwayTeam().name(), 2, 3); // Assume service updates scores to 2:3

        // Simulate user selecting "Update Match" (option 2), choosing the match, and providing new scores
        when(scanner.nextInt())
                .thenReturn(2) // User selects "Update Match" option
                .thenReturn(0) // Select the first match (index 0)
                .thenReturn(2) // Enter team 1's new score (2)
                .thenReturn(3) // Enter team 2's new score (3)
                .thenReturn(6); // Exit the CLI

        when(scanner.nextLine()).thenReturn("\n"); // To consume newline characters

        // Act: Run the CLI
        cli.run();

        // Assert: Verify that `updateMatch` was called with the correct arguments
        verify(service).updateScore(match.getHomeTeam().name(), match.getAwayTeam().name(), 2, 3);

        // Additional asserts to confirm expected console output
        assertTrue(outContent.toString().contains("Score updated."));

    }

//    @Test
//    public void testFinishMatchOperation() {
//        // Arrange: Create two matches, one active and the other finished
//        Team team1 =Team.CANADA;
//        Team team2 = Team.MEXICO;
//        Match match1 = new Match(team1, team2); // Active match
//        Match match2 = new Match(team1, team2); // Finished match
//
//        // Mock service behavior for active matches
//        List<Match> activeMatches = new ArrayList<>();
//        activeMatches.add(match1); // match1 is active
//        when(service.getLiveSummary()).thenReturn(activeMatches);
//
//        // Mock service behavior for finished matches
//        List<Match> finishedMatches = new ArrayList<>();
//        finishedMatches.add(match2); // match2 is already finished
//        when(service.getFinishedMatches()).thenReturn(finishedMatches);
//
//        // Mock the finishMatch() method
//        doAnswer(invocation -> {
//            Match match = invocation.getArgument(0);
//            // Move the match from active to finished
//            activeMatches.remove(match);
//            finishedMatches.add(match);
//            return null;
//        }).when(service).finishMatch(match1);
//
//        // Simulate the user finishing match1
//        when(scanner.nextInt())
//                .thenReturn(3) // User selects "Finish Match" option
//                .thenReturn(0) // Select the first match (index 0)
//                .thenReturn(5); // Exit operation
//        when(scanner.nextLine()).thenReturn("\n"); // Handle newline characters
//
//        // Act: Run the CLI
//        cli.run();
//
//        // Assert: Verify `finishMatch` is called with the correct match
//        verify(service).finishMatch(match1);
//
//        // Validate state of the match lists
//        assertTrue(activeMatches.isEmpty()); // match1 should be removed from active matches
//        assertTrue(finishedMatches.contains(match1)); // match1 should be added to finished matches
//        assertEquals(1, finishedMatches.size()); // Only one finished match in the list
//    }

}
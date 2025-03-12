package org.example.test1_1;


import cli.ScoreboardCLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ScoreboardService;

import java.util.Scanner;


import static org.mockito.Mockito.*;


import enums.Team;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreboardCLITests {


    private ScoreboardCLI cli;
    @Mock
    private ScoreboardService service;
    @Mock
    private Scanner scanner;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cli = new ScoreboardCLI(service, scanner);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testStartMatch_validInput() {
        // Arrange
        when(scanner.nextInt()).thenReturn(0, 1); // Select valid team indices
        when(scanner.nextLine()).thenReturn("\n"); // Consume newline
        List<Team> availableTeams = new ArrayList<>(List.of(Team.values()));
        when(service.getLiveSummary()).thenReturn(new ArrayList<>());

        // Act
        cli.run();
    System.out.println(service.getLiveSummary().toString());
        // Assert
        verify(service).startMatch(availableTeams.get(0), availableTeams.get(1));
        assertEquals("Match started: " + availableTeams.get(0) + " vs " + availableTeams.get(1) + "\n",
                outContent.toString().trim());
    }

}

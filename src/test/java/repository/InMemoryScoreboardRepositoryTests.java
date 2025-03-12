package repository;

import enums.Team;
import model.Match;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import service.ScoreboardService;


import java.util.*;

public class InMemoryScoreboardRepositoryTests {

    @Test
    public void testMatchCreation() {
        Team homeTeam = Team.MEXICO;
        Team awayTeam = Team.CANADA;
        Match match = new Match(homeTeam, awayTeam);
        assertNotNull(match);
        assertEquals(homeTeam, match.getHomeTeam());
        assertEquals(awayTeam, match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertNotNull(match.getStartTime());
    }

    @Test
    public void testMatchScoreUpdates() {
        Team homeTeam = Team.MEXICO;
        Team awayTeam = Team.CANADA;
        Match match = new Match(homeTeam, awayTeam);
        match.setHomeScore(2);
        match.setAwayScore(1);
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    public void testMatchTotalScore() {
        Team homeTeam = Team.MEXICO;
        Team awayTeam = Team.CANADA;
        Match match = new Match(homeTeam, awayTeam);
        match.setHomeScore(3);
        match.setAwayScore(4);
        assertEquals(7, match.getTotalScore());
    }

    @Test
    public void testMatchToString() {
        Team homeTeam = Team.MEXICO;
        Team awayTeam = Team.CANADA;
        Match match = new Match(homeTeam, awayTeam);
        match.setHomeScore(1);
        match.setAwayScore(2);
        String expected = String.format("%s 1 - %s 2", Team.MEXICO, Team.CANADA);
        assertEquals(expected, match.toString());
    }


    @Test
    public void testInMemoryScoreboardRepositoryStartMatch() {
        InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();
        Match match = new Match(Team.MEXICO, Team.CANADA);
        repository.startMatch(match);
        List<Match> summary = repository.getLiveSummary();
        assertEquals(1, summary.size());
    }

    @Test
    public void testInMemoryScoreboardRepositoryUpdateScore() {
        InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();
        Match match = new Match(Team.MEXICO, Team.CANADA);
        repository.startMatch(match);
        repository.updateScore(match.getHomeTeam().name(), match.getAwayTeam().name(), 3, 2);
        List<Match> summary = repository.getLiveSummary();
        assertNotNull(summary, "Summary should not be null");
        System.out.println(summary);
    }

    @Test
    public void testInMemoryScoreboardRepositoryFinishMatch() {
        InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();
        Match match = new Match(Team.MEXICO, Team.CANADA);
        repository.startMatch(match);
        repository.finishMatch(match.getHomeTeam().name(),match.getAwayTeam().name());
        List<Match> summary = repository.getLiveSummary();
        assertEquals(0, summary.size());
    }

    @Test
    public void testInMemoryScoreboardRepositoryGetSummary() {
        InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();
        Match match1 = new Match(Team.MEXICO, Team.CANADA);
        match1.setHomeScore(0);
        match1.setAwayScore(5);
        Match match2 = new Match(Team.SPAIN, Team.BRAZIL);
        match2.setHomeScore(10);
        match2.setAwayScore(2);
        Match match3 = new Match(Team.GERMANY, Team.FRANCE);
        match3.setHomeScore(2);
        match3.setAwayScore(2);
        Match match4 = new Match(Team.URUGUAY, Team.ITALY);
        match4.setHomeScore(6);
        match4.setAwayScore(6);
        Match match5 = new Match(Team.ARGENTINA, Team.AUSTRALIA);
        match5.setHomeScore(3);
        match5.setAwayScore(1);

        repository.startMatch(match1);
        repository.startMatch(match2);
        repository.startMatch(match3);
        repository.startMatch(match4);
        repository.startMatch(match5);

        List<Match> summary = repository.getLiveSummary();
        String expectedLine = "URUGUAY 6 - ITALY 6";
        Match actualLine = summary.getFirst();
        String  actualLineString = actualLine.toString();

        assertEquals(5, summary.size());

        assertTrue(actualLineString.equalsIgnoreCase(expectedLine),
                String.format("Expected '%s' (ignoring case) but got '%s'", expectedLine, actualLineString));


        assertEquals("SPAIN 10 - BRAZIL 2", summary.get(1).toString());
        assertEquals("MEXICO 0 - CANADA 5", summary.get(2).toString());
        assertEquals("ARGENTINA 3 - AUSTRALIA 1", summary.get(3).toString());
        assertEquals("GERMANY 2 - FRANCE 2", summary.get(4).toString());
    }

    @Test
    public void testScoreboardServiceStartMatch() {
        ScoreboardRepository repository = Mockito.mock(ScoreboardRepository.class);
        ScoreboardService service = new ScoreboardService(repository);
        service.startMatch(Team.MEXICO, Team.CANADA);
        Mockito.verify(repository, Mockito.times(1)).startMatch(Mockito.any(Match.class));
    }

    @Test
    public void testScoreboardServiceUpdateScore() {
        ScoreboardRepository repository = Mockito.mock(ScoreboardRepository.class);
        ScoreboardService service = new ScoreboardService(repository);
        Match match = new Match(Team.MEXICO, Team.CANADA);
        service.updateScore(match.getHomeTeam().name(), match.getAwayTeam().name(), 3, 2);
        Mockito.verify(repository, Mockito.times(1)).updateScore(match.getHomeTeam().name(), match.getAwayTeam().name(), 3, 2);
    }

    @Test
    public void testScoreboardServiceFinishMatch() {
        ScoreboardRepository repository = Mockito.mock(ScoreboardRepository.class);
        ScoreboardService service = new ScoreboardService(repository);
        Match match = new Match(Team.MEXICO, Team.CANADA);
        service.finishMatch(match.getHomeTeam().name() , match.getAwayTeam().name());
        Mockito.verify(repository, Mockito.times(1)).finishMatch(match.getHomeTeam().name() , match.getAwayTeam().name());
    }

    @Test
    public void testScoreboardServiceGetSummary() {
        ScoreboardRepository repository = Mockito.mock(ScoreboardRepository.class);
        Match match1 = new Match(Team.MEXICO, Team.CANADA);
        match1.setHomeScore(1);
        match1.setAwayScore(1);
        Match match2 = new Match(Team.SPAIN, Team.BRAZIL);
        match2.setHomeScore(2);
        match2.setAwayScore(2);
        List<Match> mockSummary = Arrays.asList(match1, match2);
        Mockito.when(repository.getLiveSummary()).thenReturn(mockSummary);
        ScoreboardService service = new ScoreboardService(repository);
        List<Match> summary = service.getLiveSummary();
        assertEquals(2, summary.size());
        assertEquals(match1, summary.get(0));
        assertEquals(match2, summary.get(1));
        Mockito.verify(repository, Mockito.times(1)).getLiveSummary();
    }

    @Test
    public void testTeamEnum() {
        assertEquals("Mexico", Team.MEXICO.getDisplayName());
        assertEquals("Canada", Team.CANADA.getDisplayName());
        assertEquals("Spain", Team.SPAIN.getDisplayName());
    }

    @Test
    public void testTeamEnumValues() {
        Team[] teams = Team.values();
        assertEquals(10, teams.length);
        assertEquals(Team.MEXICO, teams[0]);
        assertEquals(Team.CANADA, teams[1]);
        assertEquals(Team.SPAIN, teams[2]);
    }

}
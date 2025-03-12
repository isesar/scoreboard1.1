package repository;

import model.Match;

import java.util.List;


public interface ScoreboardRepository {
    void startMatch(Match match);
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);
    void finishMatch(String homeTeam, String awayTeam);
    List<Match> getLiveSummary();
    List<Match> getAllMatches();
}

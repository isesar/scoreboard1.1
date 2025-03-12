package service;

import enums.Team;
import models.Match;
import org.springframework.stereotype.Service;
import models.ScoreboardRepository;

import java.util.List;

@Service
public class ScoreboardService {
    private final ScoreboardRepository repository;

    public ScoreboardService(ScoreboardRepository repository) {
        this.repository = repository;
    }

    public void startMatch(Team homeTeam, Team awayTeam) {
        repository.startMatch(new Match(homeTeam, awayTeam));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        repository.updateScore(homeTeam, awayTeam, homeScore, awayScore);
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        repository.finishMatch(homeTeam, awayTeam);
    }

    public List<Match> getLiveSummary() {
        return repository.getLiveSummary();
    }

    public List<Match> getAllMatches(){
        return repository.getAllMatches();
    }
}

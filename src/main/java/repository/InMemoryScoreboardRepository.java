package repository;


import models.Match;
import models.ScoreboardRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryScoreboardRepository implements ScoreboardRepository {
    private final Map<String, Match> liveMatches= new ConcurrentHashMap<>();
    private final Map<String, Match> finishedMatches= new ConcurrentHashMap<>();
    @Override
    public void startMatch(Match match) {
        String key = match.getHomeTeam().name() + "-" + match.getAwayTeam().name();
        liveMatches.put(key, match);
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Match match = liveMatches.get(homeTeam + "-" + awayTeam);
        if (match != null) {
            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }

    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        liveMatches.remove(homeTeam + "-" + awayTeam);
        finishedMatches.put(homeTeam + "-" + awayTeam, liveMatches.get(homeTeam + "-" + awayTeam));
    }

    @Override
    public List<Match> getLiveSummary() {
        return liveMatches.values().stream()
                .sorted(Comparator
                        .comparingInt(Match::getTotalScore)
                        .thenComparing(Match::getStartTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Match>getAllMatches(){
        return finishedMatches.values().stream().collect(Collectors.toList());
    }
}



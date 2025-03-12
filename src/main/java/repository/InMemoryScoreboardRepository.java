package repository;

import model.Match;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryScoreboardRepository implements ScoreboardRepository {
    private final Map<String, Match> liveMatches= new ConcurrentHashMap<>();
    private final Map<String, Match> finishedMatches= new ConcurrentHashMap<>();

    /**
     * Starts tracking a new match in the live matches repository.
     * Adds the specified match to the live matches collection with a unique key
     * created by combining the names of the home and away teams.
     *
     * @param match the match to be started, containing details about the home team, away team, and initial scores
     */
    @Override
    public void startMatch(Match match) {
        String key = match.getHomeTeam().name() + "-" + match.getAwayTeam().name();
        liveMatches.put(key, match);
    }

    /**
     * Updates the score of an ongoing match between the specified home and away teams.
     * If the match exists in the live matches collection, its scores are updated.
     *
     * @param homeTeam the name of the home team participating in the match
     * @param awayTeam the name of the away team participating in the match
     * @param homeScore the updated score for the home team
     * @param awayScore the updated score for the away team
     */
    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Match match = liveMatches.get(homeTeam + "-" + awayTeam);
        if (match != null) {
            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }

    }

    /**
     * Finishes an ongoing match between the specified home and away teams.
     * Removes the match from the live matches collection and adds it to the finished matches collection.
     * If the specified match does not exist in the live matches collection, an exception is thrown.
     *
     * @param homeTeam the name of the home team participating in the match
     * @param awayTeam the name of the away team participating in the match
     * @throws IllegalArgumentException if the match does not exist or has already been finished
     */
    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        String matchKey = homeTeam + "-" + awayTeam;
        Match match = liveMatches.get(matchKey);
        if (match != null) {
            liveMatches.remove(matchKey);
            finishedMatches.put(matchKey, match);
        } else {
            throw new IllegalArgumentException("Match does not exist or has already been finished.");
        }
    }

    /**
     * This method fetches all live matches from a `ConcurrentHashMap` named `liveMatches` and sorts them based on two criteria:
     * Number of goals (total score)**: This refers to the combined scores of the home and away teams in a match. Matches with higher total scores are sorted before those with lower scores.
     * Starting time**: If two matches have the same total score, they are sorted by their starting time in descending order (**most recent matches come first**).
     */
    @Override
    public List<Match> getLiveSummary() {
        return liveMatches.values().stream()
                .sorted(Comparator
                        .comparingInt(Match::getTotalScore)
                        .thenComparing(Match::getStartTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a combined list of all live and finished matches, sorted by their start time in ascending order.
     * @return a list of all matches, including live and finished matches, sorted by start time.
     */
    public List<Match> getAllMatches() {
        return Stream.concat(liveMatches.values().stream(), finishedMatches.values().stream())
                .sorted(Comparator.comparing(Match::getStartTime))
                .collect(Collectors.toList());
    }

}



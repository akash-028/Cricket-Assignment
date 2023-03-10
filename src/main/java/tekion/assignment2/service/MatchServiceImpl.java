package tekion.assignment2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tekion.assignment2.dto.StartMatchData;
import tekion.assignment2.dao.*;
import tekion.assignment2.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final TeamRepository teamRepository;
    private final ScoreBoardRepository scoreBoardRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final CommentaryRepository commentaryRepository;
    private final MatchHelper matchHelper;

    // Start the Match
    @Override
    public ResponseEntity<Object> startMatch(StartMatchData startMatchData) {
        if (teamRepository.containsTeamId(startMatchData.getTeam1Id()))
            return new ResponseEntity<>("Team Not Found.", HttpStatus.NOT_FOUND);
        if (teamRepository.containsTeamId(startMatchData.getTeam2Id()))
            return new ResponseEntity<>("Team Not Found.", HttpStatus.NOT_FOUND);
        setUpAndPlayMatch(startMatchData);
        return new ResponseEntity<>("Match Completed and saved successfully", HttpStatus.OK);
    }

    // Setup Match, Team, Players and Then Start the match
    private void setUpAndPlayMatch(StartMatchData startMatchData) {
        MatchDetails matchDetails = setUpMatchDetails(startMatchData);
        Team team1 = setUpTeam(startMatchData.getTeam1Id());
        Team team2 = setUpTeam(startMatchData.getTeam2Id());
        toss(matchDetails, team1, team2, startMatchData.getTossResult());
    }

    // Setup Match Details
    private MatchDetails setUpMatchDetails(StartMatchData startMatchData) {
        int matchId = matchRepository.getNextMatchId();
        MatchDetails.FORMAT format = MatchDetails.FORMAT.valueOf(startMatchData.getOvers());
        return MatchDetails.builder().matchId(matchId).overs(format.getOvers())
                .team1Id(startMatchData.getTeam1Id()).team2Id(startMatchData.getTeam2Id())
                .team1Name(teamRepository.getTeamName(startMatchData.getTeam1Id()))
                .team2Name(teamRepository.getTeamName(startMatchData.getTeam2Id())).build();
    }

    // Setup Team Details
    private Team setUpTeam(int teamId) {
        List<Integer> playerIds = teamRepository.getPlayerList(teamId);
        List<Batsman> batsmen = setUpBatsmen(playerIds);
        List<Bowler> bowlers = setUpBowlers(playerIds);
        return Team.builder().teamId(teamId).teamRuns(0).teamWickets(0)
                .teamBalls(0).batsmen(batsmen).bowlers(bowlers).build();
    }

    // Setup Batsmen Details
    private List<Batsman> setUpBatsmen(List<Integer> playerIds) {
        List<Batsman> batsmen = new ArrayList<>();
        for (Integer i : playerIds) {
            Batsman batsman = Batsman.builder().playerId(i).fours(0).sixes(0).score(0).playedBalls(0).build();
            batsmen.add(batsman);
        }
        return batsmen;
    }

    // Setup Bowler Details
    private List<Bowler> setUpBowlers(List<Integer> playerIds) {
        List<Bowler> bowlers = new ArrayList<>();
        for (int i = 7; i <= 10; i++) {
            Bowler bowler = Bowler.builder().playerId(playerIds.get(i))
                    .takenWickets(0).thrownBalls(0).givenRuns(0).build();
            bowlers.add(bowler);
        }
        return bowlers;
    }

    // Save toss results
    private void toss(MatchDetails matchDetails, Team team1, Team team2, String tossResult) {
        String tossWinTeam = (tossResult.charAt(0) == '0') ? matchDetails.getTeam1Name() : matchDetails.getTeam2Name();
        matchDetails.setTossWinTeam(tossWinTeam);
        String tossWinTeamChose = (tossResult.charAt(1) == '0') ? "Bat" : "Bowl";
        matchDetails.setTossWinTeamChoose(tossWinTeamChose);
        if (tossResult.equals("01") || tossResult.equals("10")) {
            Team temp = team1;
            team1 = team2;
            team2 = temp;
        }
        playMatch(matchDetails, team1, team2);
    }

    // Match Starting
    private void playMatch(MatchDetails matchDetails, Team team1, Team team2) {
        List<Ball> inning1Commentary = startMatch(team1, team2, matchDetails.getOvers(), 1);
        List<Ball> inning2Commentary = startMatch(team2, team1, matchDetails.getOvers(), 2);
        decideWinner(matchDetails, team1, team2);
        saveMatch(matchDetails, team1, team2);
        commentaryRepository.saveCommentary(matchDetails.getMatchId(), inning1Commentary, inning2Commentary);
    }

    // Match Logic
    private List<Ball> startMatch(Team team1, Team team2, int overs, int flag) {
        List<Ball> inningCommentary = new ArrayList<>();
        matchHelper.reset();
        while (team1.getTeamBalls() < overs * 6) {
            if (flag == 2 && team2.getTeamRuns() < team1.getTeamRuns()) break;
            String type = (matchHelper.getOnStrike() <= 6) ? "Batsman" : "Bowler";
            int ballResult = HelperService.generateRandomNumberBias(type);
            Ball ball = saveCommentary(team1, team2, ballResult);
            inningCommentary.add(ball);

            saveBall(team1, team2, ballResult);
            if (team1.getTeamWickets() == 10) break;

            if (team1.getTeamBalls() % 6 == 0) {
                changeBowlerWhenOverEnds();
            }
        }
        return inningCommentary;
    }

    // Save Commentary
    private Ball saveCommentary(Team team1, Team team2, int ballResult) {
        team1.setTeamBalls(team1.getTeamBalls() + 1);
        Batsman batsman = getBatsman(team1, matchHelper.getOnStrike());
        Bowler bowler1 = getBowler(team2, matchHelper.getBowler());
        batsman.setPlayedBalls(batsman.getPlayedBalls() + 1);
        bowler1.setThrownBalls(bowler1.getThrownBalls() + 1);
        return Ball.builder().ballNumber(team1.getTeamBalls())
                .bowlerId(getBowler(team2, matchHelper.getBowler()).getPlayerId())
                .onStrikerId(getBatsman(team1, matchHelper.getOnStrike()).getPlayerId())
                .offStrikerId(getBatsman(team1, matchHelper.getOffStrike()).getPlayerId())
                .ballResult(ballResult).build();
    }

    // Get Batsman Details from team and batsman ID
    private Batsman getBatsman(Team team, int batsmanId) {
        return team.getBatsmen().get(batsmanId);
    }

    // Get Bowler Details from team and bowler ID
    private Bowler getBowler(Team team, int bowlerId) {
        return team.getBowlers().get(bowlerId);
    }

    // Save What happens on particular ball
    private void saveBall(Team team1, Team team2, int ballResult) {
        if (ballResult == 7) {
            saveWicketBall(team1, team2);
            if (team1.getTeamWickets() == 10) return;
            matchHelper.setOnStrike(matchHelper.getNextPlayer());
        } else {
            saveNormalBall(team1, team2, ballResult);
            if (ballResult % 2 == 1) {
                changeStrike();
            }
        }
    }

    // Save wicket ball
    private void saveWicketBall(Team team1, Team team2) {
        team1.setTeamWickets(team1.getTeamWickets() + 1);
        getBowler(team2, matchHelper.getBowler())
                .setTakenWickets(getBowler(team2, matchHelper.getBowler()).getTakenWickets() + 1);
        getBatsman(team1, matchHelper.getOnStrike()).setOut(true);
        getBatsman(team1, matchHelper.getOnStrike()).setOutBy(getBowler(team2, matchHelper.getBowler()).getPlayerId());
    }

    // Save normal ball
    private void saveNormalBall(Team team1, Team team2, int ballResult) {
        team1.setTeamRuns(team1.getTeamRuns() + ballResult);
        Batsman batsman = getBatsman(team1, matchHelper.getOnStrike());
        Bowler bowler1 = getBowler(team2, matchHelper.getBowler());
        batsman.setScore(batsman.getScore() + ballResult);
        bowler1.setGivenRuns(bowler1.getGivenRuns() + ballResult);
        if (ballResult == 4) batsman.setFours(batsman.getFours() + 1);
        if (ballResult == 6) batsman.setSixes(batsman.getSixes() + 1);
    }

    // Change Strike when over ends and player hits odd runs
    private void changeStrike() {
        int temp = matchHelper.getOnStrike();
        matchHelper.setOnStrike(matchHelper.getOffStrike());
        matchHelper.setOffStrike(temp);
    }

    // Change bowler when over ends
    private void changeBowlerWhenOverEnds() {
        changeStrike();
        matchHelper.setBowler(matchHelper.getNextBowler());
    }

    // Decide Winner and store results
    private void decideWinner(MatchDetails matchDetails, Team team1, Team team2) {
        String matchResult;
        if (team1.getTeamRuns() > team2.getTeamRuns()) {
            matchResult = processTeam1Win(matchDetails, team1, team2);
        } else if (team1.getTeamRuns() < team2.getTeamRuns()) {
            matchResult = processTeam2Win(matchDetails, team1, team2);
        } else {
            matchResult = processTieMatch(matchDetails, team1, team2);
        }
        matchDetails.setMatchResult(matchResult);

    }

    // Set details if team 1 wins
    private String processTeam1Win(MatchDetails matchDetails, Team team1, Team team2) {
        team1.setWonMatch(1);
        team2.setWonMatch(0);
        matchDetails.setWinner(teamRepository.getTeamName(team1.getTeamId()));
        return teamRepository.getTeamName(team1.getTeamId()) + " won the match by "
                + (team1.getTeamRuns() - team2.getTeamRuns()) + " runs.";
    }

    // Set details if team 2 wins
    private String processTeam2Win(MatchDetails matchDetails, Team team1, Team team2) {
        team1.setWonMatch(0);
        team2.setWonMatch(1);
        matchDetails.setWinner(teamRepository.getTeamName(team2.getTeamId()));
        return teamRepository.getTeamName(team2.getTeamId()) + " won the match by "
                + (10 - team2.getTeamWickets()) + " wickets.";
    }

    // Set details if match ties
    private String processTieMatch(MatchDetails matchDetails, Team team1, Team team2) {
        team1.setWonMatch(-1);
        team2.setWonMatch(-1);
        matchDetails.setWinner("No One.");
        return "Match Tied.";
    }

    // Update players and teams details and save scoreboard details
    private void saveMatch(MatchDetails matchDetails, Team team1, Team team2) {
        scoreBoardRepository.saveScoreBoard(matchDetails, team1, team2);
        playerRepository.updatePlayer(team1);
        playerRepository.updatePlayer(team2);
        playerRepository.saveMatchAndPlayer(matchDetails.getMatchId(), team1);
        playerRepository.saveMatchAndPlayer(matchDetails.getMatchId(), team2);
        teamRepository.updateTeam(team1);
        teamRepository.updateTeam(team2);
    }

}

package tekion.assignment2.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import tekion.assignment2.connection.ConnectMongo;
import tekion.assignment2.dao.Batsman;
import tekion.assignment2.dao.Bowler;
import tekion.assignment2.dao.MatchDetails;
import tekion.assignment2.dao.Team;
import tekion.assignment2.service.HelperService;

// Store and Retrieve score board details
@Repository
@RequiredArgsConstructor
class ScoreBoardRepositoryImpl implements ScoreBoardRepository {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    MongoDatabase mongoDatabase = ConnectMongo.getConnection();
    MongoCollection<Document> scoreBoardCollection = mongoDatabase.getCollection("scoreBoard");


     // Function for store score board in database
     @Override
     public void saveScoreBoard(MatchDetails matchDetails, Team team1, Team team2)
     {
         Document match = new Document();
         match.append("matchId", matchDetails.getMatchId())
                 .append("team1Name", teamRepository.getTeamName(team1.getTeamId()))
                 .append("team2Name", teamRepository.getTeamName(team2.getTeamId()))
                 .append("tossWinTeam", matchDetails.getTossWinTeam())
                 .append("tossWinTeamChoose", matchDetails.getTossWinTeamChoose());
         Document innings1 = saveInnings(team1, team2);
         Document innings2 = saveInnings(team2, team1);
         match.append("innings1", innings1).append("innings2", innings2)
                 .append("winner", matchDetails.getWinner())
                 .append("matchResult", matchDetails.getMatchResult());
         scoreBoardCollection.insertOne(match);
     }

    // Helper function which stores innings
    private Document saveInnings(Team team1, Team team2) {
        Document inning = new Document();
        Document batting = new Document();
        Document bowling = new Document();
        Document inningResult = inningsResult(team1);
        for (int i = 0; i < 11; i++) {
            String s = "Player" + (i + 1);
            batting.append(s, saveBatting(team1, i));
        }
        for (int i = 7; i < 11; i++) {
            String s = "Player" + (i + 1);
            bowling.append(s, saveBowling(team2, i-7));
        }
        inning.append("batting", batting).append("bowling", bowling).append("score", inningResult);
        return inning;
    }

    // Helper function which stores batting details
    private Document saveBatting(Team team, int playerNum) {
        Batsman batsman = team.getBatsmen().get(playerNum);
        Document player = new Document();
        player.append("playerId",batsman.getPlayerId())
                .append("playerName", playerRepository.getPlayerName(batsman.getPlayerId()))
                .append("runs", batsman.getScore()).append("balls", batsman.getPlayedBalls())
                .append("4s", batsman.getFours()).append("6s", batsman.getSixes());
        if(batsman.isOut())
        {
            player.append("outBy", batsman.getOutBy()) ;
        }
        return player;
    }

    // Helper function which stores bowling details
    private Document saveBowling(Team team, int playerNum) {
        Bowler bowler = team.getBowlers().get(playerNum);
        Document player = new Document();
        player.append("playerId",bowler.getPlayerId())
                .append("playerName", playerRepository.getPlayerName(bowler.getPlayerId()))
                .append("givenRuns", bowler.getGivenRuns()).append("balls", bowler.getThrownBalls())
                .append("wickets", bowler.getTakenWickets())
        ;
        return player;
    }

    // Helper function which stores result of the match
    private Document inningsResult(Team team) {
        Document result = new Document();
        String overs = HelperService.convertToOvers(team.getTeamBalls());
        result.append("runs", team.getTeamRuns()).append("wickets", team.getTeamWickets())
                .append("overs", Double.parseDouble(overs));
        return result;
    }


    // Returns scoreboard for a particular match ID
    @Override
    public Document getScoreBoard(int matchId) {
        return scoreBoardCollection.find(Filters.eq("matchId", matchId)).first();
    }

}

package tekion.assignment2.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import tekion.assignment2.connection.ConnectMongo;
import tekion.assignment2.dao.Batsman;
import tekion.assignment2.dao.Bowler;
import tekion.assignment2.dao.Team;

import java.util.ArrayList;
import java.util.List;


// Store and Retrieve player details
@Repository
@RequiredArgsConstructor
class PlayerRepositoryImpl implements PlayerRepository {
    MongoDatabase database = ConnectMongo.getConnection();
    MongoCollection<Document> playerCollection = database.getCollection("players");
    MongoCollection<Document> playerAndMatchCollection = database.getCollection("playerAndMatch");

    private final TeamRepository teamRepository;

    // Function to check player exist or not in database
    @Override
    public boolean containsPlayerId(int playerId) {
        Document doc = playerCollection.find(Filters.eq("playerId", playerId)).first();
        return doc == null;
    }

    // Function to check if player is present in specific match or not
    @Override
    public boolean containsPlayerAndMatchId(int playerId, int matchId) {
        Bson filter1 = Filters.eq("playerId", playerId);
        Bson filter2 = Filters.eq("matchId", matchId);
        Document doc = playerAndMatchCollection.find(Filters.and(filter1, filter2)).first();
        return doc == null;
    }

    // Save player details for particular match
    @Override
    public void saveMatchAndPlayer(int matchId, Team team) {

        List<Batsman> batsmen = team.getBatsmen();
        List<Bowler> bowlers = team.getBowlers();
        for (int i = 0; i < 11; i++) {
            Document playerAndMatch = new Document();
            saveMatchAndBatsman(matchId, team, batsmen, i, playerAndMatch);
            if (i > 6) {
                saveMatchAndBowler(bowlers, i, playerAndMatch);
            }
            playerAndMatchCollection.insertOne(playerAndMatch);
        }
    }

    // Save Batsman for particular Match
    private void saveMatchAndBatsman(int matchId, Team team, List<Batsman> batsmen, int playerNum, Document playerAndMatch) {
        Batsman batsmanDetails = batsmen.get(playerNum);
        playerAndMatch.append("playerId", batsmanDetails.getPlayerId())
                .append("matchId", matchId)
                .append("playerName", getPlayerName(batsmanDetails.getPlayerId()))
                .append("playerTeam", teamRepository.getTeamName(team.getTeamId()))
                .append("score", batsmanDetails.getScore())
                .append("playedBalls", batsmanDetails.getPlayedBalls())
                .append("4s", batsmanDetails.getFours())
                .append("6s", batsmanDetails.getSixes());
        if (batsmanDetails.isOut()) {
            playerAndMatch.append("outBy", batsmanDetails.getOutBy());
        }
    }

    // Save bowler for particular match
    private void saveMatchAndBowler(List<Bowler> bowlers, int playerNum, Document playerAndMatch) {
        Bowler bowlerDetails = bowlers.get(playerNum - 7);
        playerAndMatch.append("thrownBalls", bowlerDetails.getThrownBalls())
                .append("givenRuns", bowlerDetails.getGivenRuns())
                .append("takenWickets", bowlerDetails.getTakenWickets());
    }

    // Update players total details
    @Override
    public void updatePlayer(Team team) {
        List<Batsman> batsmen = team.getBatsmen();
        List<Bowler> bowler = team.getBowlers();
        for (Batsman b : batsmen) {
            updateBatsman(b);
        }
        for (Bowler b : bowler) {
            updateBowler(b);
        }
    }

    // Update Batsman Details
    private void updateBatsman(Batsman b) {
        int id = b.getPlayerId();
        increasePlayerTotalDetails(id, b.getScore(), "totalScore");
        increasePlayerTotalDetails(id, b.getPlayedBalls(), "totalPlayedBalls");
        increasePlayerTotalDetails(id, b.getFours(), "total4s");
        increasePlayerTotalDetails(id, b.getSixes(), "total6s");
        if (b.isOut()) {
            increasePlayerTotalDetails(id, 1, "totalOut");
        }
    }

    // Update Bowler Details
    private void updateBowler(Bowler b) {
        int id = b.getPlayerId();
        increasePlayerTotalDetails(id, b.getGivenRuns(), "totalGivenRuns");
        increasePlayerTotalDetails(id, b.getTakenWickets(), "totalTakenWickets");
        increasePlayerTotalDetails(id, b.getThrownBalls(), "totalThrownBalls");
    }

    // General function for increase in player(both batsman and bowler) details
    @Override
    public void increasePlayerTotalDetails(int playerId, int increment, String incrementString) {
        Bson filter1 = Filters.eq("playerId", playerId);
        playerCollection.updateOne(filter1, Updates.inc(incrementString, increment));
    }

    // Function for get player name
    @Override
    public String getPlayerName(int playerId) {
        List<Document> players = playerCollection.find(Filters.eq("playerId", playerId)).into(new ArrayList<>());
        return (String) players.get(0).get("playerName");
    }

    // Function for get player
    @Override
    public Document getPlayer(int playerId) {
        return playerCollection.find(Filters.eq("playerId", playerId)).first();
    }

    // Function for get players details as batsman and bowler in a particular match
    @Override
    public Document getPlayerAndMatch(int playerId, int matchId) {
        Bson filter1 = Filters.eq("playerId", playerId);
        Bson filter2 = Filters.eq("matchId", matchId);
        return playerAndMatchCollection.find(Filters.and(filter1, filter2)).first();
    }
}

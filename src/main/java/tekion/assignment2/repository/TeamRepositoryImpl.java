package tekion.assignment2.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import tekion.assignment2.connection.ConnectMongo;
import tekion.assignment2.dao.Team;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.gte;


// Store and Retrieve Team details
@Repository
class TeamRepositoryImpl implements TeamRepository {
    MongoDatabase database = ConnectMongo.getConnection();
    MongoCollection<Document> teamCollection = database.getCollection("teams");
    MongoCollection<Document> playerCollection = database.getCollection("players");

    // Function to check team exist or not in database
    @Override
    public boolean containsTeamId(int teamId) {
        Document doc = teamCollection.find(Filters.eq("teamId", teamId)).first();
        return doc == null;
    }

    // Save team details in database
    @Override
    public void saveTeam(String[] s) {
        int playerId = getNextPlayerId();
        int teamId = getNextTeamId();
        Document playerIds = new Document();

        for (int i = 1; i <= 11; i++) {
            String type = (i <= 7) ? "Batsman" : "Bowler";
            Document player = new Document();
            createBatsman(player, type, s[0], s[i], playerId);
            if (i > 7) {
                createBowler(player);
            }
            playerCollection.insertOne(player);
            playerIds.append(String.valueOf(i), playerId++);
        }
        Document teamDoc = new Document();
        createTeam(teamDoc, teamId, s[0], playerIds);
        teamCollection.insertOne(teamDoc);
    }

    // Create document for batsman
    private void createBatsman(Document player, String type, String teamName, String playerName, int playerId) {
        player.append("playerId", playerId).append("playerName", playerName).append("playerType", type)
                .append("playerTeam", teamName).append("totalScore", 0).append("totalPlayedBalls", 0)
                .append("total4s", 0).append("total6s", 0).append("totalOut", 0);
    }

    // Create document for bowler
    private void createBowler(Document player) {
        player.append("totalThrownBalls", 0).append("totalGivenRuns", 0)
                .append("totalTakenWickets", 0);
    }

    // Create document for team
    private void createTeam(Document teamDoc, int teamId, String teamName, Document playerIds) {
        teamDoc.append("teamId", teamId).append("teamName", teamName).append("teamTotalMatch", 0)
                .append("teamTotalMatchWins", 0).append("teamTotalMatchLosses", 0).append("teamTotalMatchTied", 0)
                .append("players", playerIds);
    }

    //  General function for increase in team details like team total matches, wins and losses
    @Override
    public void increaseTeamDetails(int teamId, int increment, String increaseString) {
        Bson filter = Filters.eq("teamId", teamId);
        teamCollection.updateOne(filter, Updates.inc(increaseString, increment));
    }

    // Update team wins and loses details
    @Override
    public void updateTeam(Team team) {
        increaseTeamDetails(team.getTeamId(), 1, "teamTotalMatch");
        String s = "teamTotalMatchTied";
        if (team.getWonMatch() == 1) s = "teamTotalMatchWins";
        else if (team.getWonMatch() == 0) s = "teamTotalMatchLosses";
        increaseTeamDetails(team.getTeamId(), 1, s);
    }

    // Returns the team for a particular team ID
    @Override
    public Document getTeam(int teamId) {
        return teamCollection.find(Filters.eq("teamId", teamId)).first();
    }

    // Returns the team name for a team ID
    @Override
    public String getTeamName(int teamId) {
        List<Document> players = teamCollection.find(Filters.eq("teamId", teamId)).limit(1).into(new ArrayList<>());
        return (String) players.get(0).get("teamName");
    }

    // Returns List of players in a team
    @Override
    public List<Integer> getPlayerList(int teamId) {
        Document players = teamCollection.find(Filters.eq("teamId", teamId)).first();
        assert players != null;
        Document playerIds = (Document) players.get("players");
        List<Integer> playerIdsFromDatabase = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            Integer p = (Integer) playerIds.get(String.valueOf(i));
            playerIdsFromDatabase.add(p);
        }
        return playerIdsFromDatabase;
    }

    // Returns next available player id in database
    private int getNextPlayerId() {
        List<Document> players = playerCollection.find(gte("playerId", 0)).limit(1).into(new ArrayList<>());
        return (players.size() == 0) ? 0 : (int) players.get(0).get("playerId") + 1;
    }

    // Returns next available team id in database
    private int getNextTeamId() {
        List<Document> team = teamCollection.find(gte("teamId", 0)).limit(1).into(new ArrayList<>());
        return (team.size() == 0) ? 0 : (int) team.get(0).get("teamId") + 1;
    }
}

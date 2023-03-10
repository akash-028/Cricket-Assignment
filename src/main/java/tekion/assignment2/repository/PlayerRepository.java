package tekion.assignment2.repository;

import org.bson.Document;
import tekion.assignment2.dao.Team;

public interface PlayerRepository {
    boolean containsPlayerId(int playerId);

    boolean containsPlayerAndMatchId(int playerId, int matchId);

    void saveMatchAndPlayer(int matchId, Team team);

    void updatePlayer(Team team);

    void increasePlayerTotalDetails(int playerId, int increment, String incrementString);

    String getPlayerName(int playerId);

    Document getPlayer(int playerId);

    Document getPlayerAndMatch(int playerId, int matchId);

}

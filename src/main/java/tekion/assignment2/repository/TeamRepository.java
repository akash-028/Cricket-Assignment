package tekion.assignment2.repository;

import org.bson.Document;
import tekion.assignment2.dao.Team;

import java.util.List;

public interface TeamRepository {
    boolean containsTeamId(int teamId);

    void saveTeam(String[] s);

    void increaseTeamDetails(int teamId, int increment, String increaseString);

    void updateTeam(Team team);

    Document getTeam(int teamId);

    String getTeamName(int teamId);

    List<Integer> getPlayerList(int teamId);
}

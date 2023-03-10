package tekion.assignment2.repository;

import org.bson.Document;
import tekion.assignment2.dao.MatchDetails;
import tekion.assignment2.dao.Team;

public interface ScoreBoardRepository {

    void saveScoreBoard(MatchDetails matchDetails, Team team1, Team team2);

    Document getScoreBoard(int matchId);

}

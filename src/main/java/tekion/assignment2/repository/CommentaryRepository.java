package tekion.assignment2.repository;

import org.bson.Document;
import tekion.assignment2.dao.Ball;

import java.util.List;

public interface CommentaryRepository {

    void saveCommentary(int matchId, List<Ball> inning1Commentary, List<Ball> inning2Commentary) ;
    Document getMatchCommentaryDetails(int matchId);
    Document getBallDetails(int matchId, int inningId, int ballNumber);
}

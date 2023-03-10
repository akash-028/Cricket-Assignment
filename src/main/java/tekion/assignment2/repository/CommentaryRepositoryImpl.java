package tekion.assignment2.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import tekion.assignment2.connection.ConnectMongo;
import tekion.assignment2.dao.Ball;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
class CommentaryRepositoryImpl implements CommentaryRepository {
    private final PlayerRepository playerRepository ;
    MongoDatabase mongoDatabase = ConnectMongo.getConnection();
    MongoCollection<Document> commentaryCollection = mongoDatabase.getCollection("commentary");

    @Override
    public void saveCommentary(int matchId, List<Ball> inning1Commentary, List<Ball> inning2Commentary)
    {
        Document commentary = new Document() ;
        commentary.append("matchId", matchId) ;
        Document inning1 = saveInning(inning1Commentary);
        Document inning2 = saveInning(inning2Commentary);
        commentary.append("inning1",inning1).append("inning2",inning2) ;
        commentaryCollection.insertOne(commentary);
    }

    // Generate Inning Document for commentary
    private Document saveInning(List<Ball> balls)
    {
        Document inning = new Document() ;
        for(Ball b:balls)
        {
            Document ball = generateBallDocument(b) ;
            String s = "ball"+ b.getBallNumber();
            inning.append(s,ball) ;
        }
        return inning ;
    }

    // Generate Ball Document
    private Document generateBallDocument(Ball c)
    {
        Document ball = new Document() ;
        ball.append("bowlerId",c.getBowlerId())
                .append("bowlerName",playerRepository.getPlayerName(c.getBowlerId()))
                .append("onStrikeId",c.getOnStrikerId())
                .append("onStrikeName",playerRepository.getPlayerName(c.getOnStrikerId()))
                .append("offStrikeId",c.getOffStrikerId())
                .append("offStrikeName",playerRepository.getPlayerName(c.getOffStrikerId()))
                .append("ballResult",c.getBallResult()) ;
        return ball ;
    }

    // Returns commentary of the particular match
    @Override
    public Document getMatchCommentaryDetails(int matchId) {
        return commentaryCollection.find(Filters.eq("matchId", matchId)).first();
    }

    // Returns details of what happens at that ball of an inning in a particular match
    @Override
    public Document getBallDetails(int matchId, int inningId, int ballNumber) {
        Document match = commentaryCollection.find(Filters.eq("matchId", matchId)).first();
        String inning = "inning" + inningId ;
        Document inningDetails = (Document) Objects.requireNonNull(match).get(inning);
        String ball = "ball" + (ballNumber);
        return (Document) inningDetails.get(ball);
    }
}

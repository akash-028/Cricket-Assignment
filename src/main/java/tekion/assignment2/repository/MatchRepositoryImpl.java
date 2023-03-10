package tekion.assignment2.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import tekion.assignment2.connection.ConnectMongo;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.gte;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepository {
    MongoDatabase mongoDatabase = ConnectMongo.getConnection();
    MongoCollection<Document> scoreBoardCollection = mongoDatabase.getCollection("scoreBoard");

    // Function to check score board exist or not in database
    @Override
    public boolean containsMatchId(int matchId) {
        Document doc = scoreBoardCollection.find(Filters.eq("matchId", matchId)).first();
        return doc == null;
    }

    //  Function to get next match ID
    @Override
    public int getNextMatchId() {
        List<Document> matchSize = scoreBoardCollection.find(gte("matchId", 0)).into(new ArrayList<>());
        return (matchSize.size() == 0) ? 0 : (int) matchSize.get(0).get("matchId") + 1;
    }
}

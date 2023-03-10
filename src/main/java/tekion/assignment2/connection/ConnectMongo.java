package tekion.assignment2.connection;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

public class ConnectMongo {

    static MongoClient mongoClient = new MongoClient("localhost", 27017);
    static IndexOptions indexOptions = new IndexOptions().unique(true);

    // Connect to MongoDatabase Assignment
    public static MongoDatabase getConnection() {

        return mongoClient.getDatabase("Assignment");
    }

    // Create Indexes in Collections
    public static void createIndexes() {
        // Create Single Indexes on collections "teams, players, scoreboard, commentary" and Multiple
        // Indexes on collections "batsman and bowler
        MongoDatabase mongoDatabase = ConnectMongo.getConnection();
        MongoCollection<Document> collection1 = mongoDatabase.getCollection("teams");
        MongoCollection<Document> collection2 = mongoDatabase.getCollection("players");
        MongoCollection<Document> collection3 = mongoDatabase.getCollection("scoreBoard");
        MongoCollection<Document> collection4 = mongoDatabase.getCollection("playerAndMatch");
        MongoCollection<Document> collection5 = mongoDatabase.getCollection("commentary");
        collection1.createIndex(Indexes.descending("teamId"), indexOptions);
        collection2.createIndex(Indexes.descending("playerId"), indexOptions);
        collection3.createIndex(Indexes.descending("matchId"), indexOptions);
        collection4.createIndex(Indexes.compoundIndex(Indexes.descending("playerId"), Indexes.descending("matchId")), indexOptions);
        collection5.createIndex(Indexes.descending("matchId"), indexOptions);
    }
}

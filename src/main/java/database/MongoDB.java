package database;

import com.mongodb.ReadConcern;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.NonNull;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
public class MongoDB extends Database {

    protected final ClientSession session;
    protected final MongoClient mongoClient;
    protected Set<MongoCollection<Document>> collections;

    public MongoDB(String dbName, Set<String> names, @NonNull MongoClient mongoClient) {
        super(dbName, names);
        this.mongoClient = mongoClient;
        session = mongoClient.startSession();
        setUpSession(session);
    }

    public Set<MongoCollection<Document>> getAllCollections() {
        if (collections == null) {
            collections = new HashSet<>();
            var db = mongoClient.getDatabase(this.name);
            for (var name : db.listCollectionNames()) {
                if (names == null || (names.contains(name))) collections.add(db.getCollection(name));
            }
        }
        return collections;
    }

    public void setUpSession(ClientSession session) {
        session.startTransaction(TransactionOptions.builder()
                .readConcern(ReadConcern.SNAPSHOT)
                .writeConcern(WriteConcern.MAJORITY)
                .build());
    }

    @Override
    public void completeConversionWithError() {
        session.abortTransaction();
        session.close();
        mongoClient.close();
    }

    @Override
    public void completeConversionWithSuccess() {
        session.commitTransaction();
        session.close();
        mongoClient.close();
    }
}

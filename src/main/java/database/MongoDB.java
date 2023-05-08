package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.NonNull;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
public class MongoDB extends Database {

    protected final MongoClient mongoClient;
    protected Set<MongoCollection<Document>> collections;

    public MongoDB(@NonNull MongoClient mongoClient, String dbName, Set<String> names) {
        super(dbName, names);
        this.mongoClient = mongoClient;
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

    @Override
    public void lock() {

    }

    @Override
    public void unLock() {

    }
}

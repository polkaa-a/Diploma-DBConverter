package dto.mongodb;

import database.MongoDB;
import dto.base.DatabaseDTO;
import lombok.Getter;

import java.util.Set;

@Getter
public class MongoDBDatabaseDTO extends DatabaseDTO<MongoDB> {
    private final Set<MongoDBCollectionDTO> collections;

    public MongoDBDatabaseDTO(String name, Set<MongoDBCollectionDTO> collections) {
        super(name);
        this.collections = collections;
    }
}

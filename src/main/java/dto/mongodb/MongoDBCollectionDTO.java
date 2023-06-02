package dto.mongodb;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MongoDBCollectionDTO {

    protected final String name;
    protected final List<MongoDBDocumentDTO> documents;

    public MongoDBCollectionDTO(@NonNull String name) {
        this.name = name;
        documents = new ArrayList<>();
    }

    public void addDocument(@NonNull MongoDBDocumentDTO document) {
        documents.add(document);
    }
}

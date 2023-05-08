package dto.mongodb;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class MongoDBCollectionDTO {

    private final String name;
    private final List<MongoDBDocumentDTO> documents;

    public MongoDBCollectionDTO(@NonNull String name, @NonNull List<MongoDBDocumentDTO> documents) {
        this.name = name;
        this.documents = documents;
        for (MongoDBDocumentDTO documentDTO : documents) {
            documentDTO.setMongoDBCollectionDTO(this);
        }
    }
}

package dto.mongodb;

import dto.base.FieldsKeeperDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
public class MongoDBDocumentDTO extends FieldsKeeperDTO<MongoDBFieldDTO> {

    @NonNull
    private final Document document;
    private final Map<String, MongoDBDocumentDTO> subDocuments;

    @Getter
    private MongoDBCollectionDTO mongoDBCollectionDTO;

    public MongoDBDocumentDTO(Document document) {
        super();
        this.document = document;
        subDocuments = new HashMap<>();
    }

    @Override
    public boolean addField(@NonNull MongoDBFieldDTO fieldDTO) {
        fieldDTO.setMongoDBDocumentDTO(this);
        return super.addField(fieldDTO);
    }

    public boolean setMongoDBCollectionDTO(@NonNull MongoDBCollectionDTO mongoDBCollectionDTO) {
        if (this.mongoDBCollectionDTO == null) {
            this.mongoDBCollectionDTO = mongoDBCollectionDTO;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var documentDTO = (MongoDBDocumentDTO) o;
        return documentDTO.getFields().containsAll(this.fields) &&
                this.fields.containsAll(documentDTO.getFields());
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

}

package dto.mongodb;

import dto.base.FieldDTO;
import lombok.Getter;
import lombok.NonNull;
import org.bson.BsonType;

@Getter
public class MongoDBFieldDTO extends FieldDTO {
    @NonNull
    private final BsonType bsonType;
    @NonNull
    private final boolean isId;

    @Getter
    //document that has this field
    private MongoDBDocumentDTO mongoDBDocumentDTO;

    public MongoDBFieldDTO(String name, BsonType bsonType, boolean isId, MongoDBDocumentDTO documentDTO) {
        super(name, documentDTO);
        this.bsonType = bsonType;
        this.isId = isId;
    }

    @Override
    public Object getValue() {
        return mongoDBDocumentDTO.getDocument().get(originalName);
    }

    public boolean setMongoDBDocumentDTO(@NonNull MongoDBDocumentDTO mongoDBDocumentDTO) {
        if (this.mongoDBDocumentDTO == null) {
            this.mongoDBDocumentDTO = mongoDBDocumentDTO;
            return true;
        }
        return false;
    }

    //by name and type
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MongoDBFieldDTO fieldDTO = (MongoDBFieldDTO) o;

        return bsonType == fieldDTO.bsonType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + bsonType.hashCode();
        return result;
    }
}

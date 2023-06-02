package dto.mongodb;

import dto.base.FieldDTO;
import lombok.Getter;
import lombok.NonNull;
import org.bson.BsonType;

@Getter
public class MongoDBFieldDTO extends FieldDTO {
    @NonNull
    protected final boolean isId;

    public MongoDBFieldDTO(String name, BsonType bsonType, boolean isId, MongoDBDocumentDTO documentDTO) {
        super(name, documentDTO, bsonType);
        this.isId = isId;
        documentDTO.addField(this);
    }

    public Object getValue() {
        return ((MongoDBDocumentDTO) fieldsKeeperDTO).getDocument().get(originalName);
    }

    //by name and type
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MongoDBFieldDTO fieldDTO = (MongoDBFieldDTO) o;
        return type == fieldDTO.type;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}

package dto.mongodb;

import dto.base.FieldDTO;
import dto.base.FieldsKeeperDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.*;

@Getter
@Slf4j
public class MongoDBDocumentDTO extends FieldsKeeperDTO<MongoDBFieldDTO> {

    @NonNull
    protected final Document document;
    protected final Map<String, MongoDBDocumentDTO> subDocuments;
    protected final MongoDBCollectionDTO mongoDBCollectionDTO;

    public MongoDBDocumentDTO(@NonNull Document document, @NonNull MongoDBCollectionDTO collectionDTO) {
        super();
        this.document = document;
        this.mongoDBCollectionDTO = collectionDTO;
        subDocuments = new HashMap<>();
    }

    @Override
    public List<Map<FieldDTO, Object>> getValues() {
        var values = new LinkedHashMap<FieldDTO, Object>();
        var list = new ArrayList<Map<FieldDTO, Object>>();
        fields.forEach(f -> values.put(f, f.getValue()));
        list.add(values);
        return list;
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

package conversion.dtotodto.mongotopostgres;

import conversion.types.PostgresSQLTypesConverter;
import dto.mongodb.MongoDBDocumentDTO;
import dto.postgresql.PostgreSQLTableDTO;
import org.bson.BsonType;

public class IdenticalDocumentsToTableConverter extends MongoDBDTOToPostgreSQLDTOConverter {

    @Override
    protected boolean isCompatibleTableForDocument(PostgreSQLTableDTO tableDTO, MongoDBDocumentDTO documentDTO) {
        var sameSize = tableDTO.getFields().size() == getDocumentFieldsThatCanBeMapped(documentDTO).count();
        var sameCollection = super.isCompatibleTableForDocument(tableDTO, documentDTO);
        if (!sameSize || !sameCollection) return false;
        for (var docField : getDocumentFieldsThatCanBeMapped(documentDTO).toList()) {
            var type = PostgresSQLTypesConverter.convert((BsonType) docField.getType());
            var name = docField.getOriginalName();
            var fields = getIdenticalFields(tableDTO, name, type);
            if (fields.toList().isEmpty()) return false;
        }
        return true;
    }
}

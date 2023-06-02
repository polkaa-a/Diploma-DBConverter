package conversion.dtotodto.mongotopostgres;

import conversion.types.PostgresSQLTypesConverter;
import dto.mongodb.MongoDBDocumentDTO;
import dto.postgresql.PostgreSQLTableDTO;
import exceptions.ConversionException;
import org.bson.BsonType;

import java.util.stream.Stream;


public class SimilarDocumentsToTableConverter extends MongoDBDTOToPostgreSQLDTOConverter {

    @Override
    protected void addDocumentAsSource(PostgreSQLTableDTO tableDTO, MongoDBDocumentDTO documentDTO) throws ConversionException {
        updateSimilarFields(tableDTO, documentDTO);
        super.addDocumentAsSource(tableDTO, documentDTO);
    }

    @Override
    protected boolean isCompatibleTableForDocument(PostgreSQLTableDTO tableDTO, MongoDBDocumentDTO documentDTO) {
        var sameSize = tableDTO.getFields().size() == getDocumentFieldsThatCanBeMapped(documentDTO).count();
        var sameCollection = super.isCompatibleTableForDocument(tableDTO, documentDTO);
        if (!sameSize || !sameCollection) return false;
        for (var docField : getDocumentFieldsThatCanBeMapped(documentDTO).toList()) {
            var type = PostgresSQLTypesConverter.convert((BsonType) docField.getType());
            var name = docField.getOriginalName();
            var fields = Stream.concat(getSimilarFields(tableDTO, name, type), getIdenticalFields(tableDTO, name, type));
            if (fields.toList().isEmpty()) return false;
        }
        return true;
    }
}

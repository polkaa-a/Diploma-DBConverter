package conversion.dtotodto.mongotopostgres;

import conversion.types.PostgresSQLTypesConverter;
import dto.mongodb.MongoDBDocumentDTO;
import dto.postgresql.PostgreSQLTableDTO;

import java.util.stream.Stream;


public class SimilarDocumentsToTableConverter extends MongoDBDTOToPostgreSQLDTOConverter {

    @Override
    protected void addAsSource(PostgreSQLTableDTO tableDTO, MongoDBDocumentDTO documentDTO) {
        updateSimilarFields(tableDTO, documentDTO);
        super.addAsSource(tableDTO, documentDTO);
    }

    @Override
    protected boolean isCompatible(PostgreSQLTableDTO tableDTO, MongoDBDocumentDTO documentDTO) {
        var sameSize = tableDTO.getFields().size() == getDocumentFieldsThatCanBeMapped(documentDTO).count();
        var sameCollection = super.isCompatible(tableDTO, documentDTO);
        if (!sameSize || !sameCollection) return false;
        for (var docField : getDocumentFieldsThatCanBeMapped(documentDTO).toList()) {
            var type = PostgresSQLTypesConverter.convert(docField.getBsonType());
            var name = docField.getOriginalName();
            var fields = Stream.concat(getSimilarFields(tableDTO, name, type), getIdenticalFields(tableDTO, name, type));
            if (fields.toList().isEmpty()) return false;
        }
        return true;
    }
}

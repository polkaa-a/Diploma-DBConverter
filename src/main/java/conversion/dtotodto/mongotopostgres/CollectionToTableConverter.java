package conversion.dtotodto.mongotopostgres;

import dto.mongodb.MongoDBDocumentDTO;
import dto.postgresql.PostgreSQLTableDTO;
import exceptions.ConversionException;

public class CollectionToTableConverter extends MongoDBDTOToPostgreSQLDTOConverter {

    @Override
    protected void
    addDocumentAsSource(PostgreSQLTableDTO tableDTO, MongoDBDocumentDTO documentDTO) throws ConversionException {
        updateSimilarFields(tableDTO, documentDTO);
        updateNewFields(tableDTO, documentDTO);
        super.addDocumentAsSource(tableDTO, documentDTO);
    }
}

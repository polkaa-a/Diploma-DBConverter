package conversion.dtotodto.mongotopostgres;

import dto.mongodb.MongoDBDocumentDTO;
import dto.postgresql.PostgreSQLTableDTO;

public class CollectionToTableConverter extends MongoDBDTOToPostgreSQLDTOConverter {

    @Override
    protected void addAsSource(PostgreSQLTableDTO tableDTO, MongoDBDocumentDTO documentDTO) {
        updateSimilarFields(tableDTO, documentDTO);
        updateNewFields(tableDTO, documentDTO);
        super.addAsSource(tableDTO, documentDTO);
    }
}

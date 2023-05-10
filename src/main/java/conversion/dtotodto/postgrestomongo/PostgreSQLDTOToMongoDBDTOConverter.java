package conversion.dtotodto.postgrestomongo;

import conversion.dtotodto.DTOToDTOConverter;
import dto.mongodb.MongoDBDatabaseDTO;
import dto.postgresql.PostgreSQLDatabaseDTO;

public class PostgreSQLDTOToMongoDBDTOConverter implements DTOToDTOConverter<PostgreSQLDatabaseDTO, MongoDBDatabaseDTO> {

    @Override
    public MongoDBDatabaseDTO convert(PostgreSQLDatabaseDTO postgreSQLDatabaseDTO) {
        return null;
    }
}

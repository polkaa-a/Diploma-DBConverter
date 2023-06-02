package conversion.manager;

import conversion.databasetodto.document.MongoDBToDTOConverter;
import conversion.dtotodatabase.relational.DTOToPostgreSQLConverter;
import conversion.dtotodto.DTOToDTOConverter;
import database.MongoDB;
import database.PostgreSQL;
import dto.mongodb.MongoDBDatabaseDTO;
import dto.postgresql.PostgreSQLDatabaseDTO;
import exceptions.ConversionException;
import lombok.NonNull;

public class MongoDBToPostgreSQLDBConverterManager {
    private final DTOToDTOConverter<MongoDBDatabaseDTO, PostgreSQLDatabaseDTO> dtoToDTOConverter;

    public MongoDBToPostgreSQLDBConverterManager(@NonNull DTOToDTOConverter<MongoDBDatabaseDTO, PostgreSQLDatabaseDTO> dtoToDTOConverter) {
        this.dtoToDTOConverter = dtoToDTOConverter;
    }

    public void convert(@NonNull MongoDB mongoDB, @NonNull PostgreSQL postgreSQL) throws ConversionException {
        MongoDBToDTOConverter dbToDTOConverter = new MongoDBToDTOConverter();
        DTOToPostgreSQLConverter dtoToDbConverter = new DTOToPostgreSQLConverter();

        DBConverterManager<MongoDBDatabaseDTO, PostgreSQLDatabaseDTO, MongoDB, PostgreSQL> converterManager =
                new DBConverterManager<>(dbToDTOConverter, dtoToDTOConverter, dtoToDbConverter);

        converterManager.convert(mongoDB, postgreSQL);
    }
}

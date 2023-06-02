package conversion.dtotodatabase.document;

import conversion.dtotodatabase.DTOToDBConverter;
import database.MongoDB;
import dto.mongodb.MongoDBDatabaseDTO;
import exceptions.ConversionException;

//MongoDBConverter converts MongoDBDatabaseDTO to MongoDB
public class DTOToMongoDBConverter implements DTOToDBConverter<MongoDBDatabaseDTO, MongoDB> {

    @Override
    public void convert(MongoDBDatabaseDTO mongoDBDatabaseDTO, MongoDB database) throws ConversionException {
    }
}

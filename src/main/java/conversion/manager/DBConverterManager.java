package conversion.manager;

import conversion.databasetodto.DBToDTOConverter;
import conversion.dtotodatabase.DTOToDBConverter;
import conversion.dtotodto.DTOToDTOConverter;
import database.Database;
import dto.base.DatabaseDTO;
import exceptions.ConversionException;
import lombok.NonNull;

public class DBConverterManager<DtoFrom extends DatabaseDTO<DbFrom>, DtoTo extends DatabaseDTO<DbTo>,
        DbFrom extends Database, DbTo extends Database> {

    private final DBToDTOConverter<DbFrom, DtoFrom> dbToDTOConverter;
    private final DTOToDTOConverter<DtoFrom, DtoTo> dtoToDTOConverter;
    private final DTOToDBConverter<DtoTo, DbTo> dtoToDBConverter;

    public DBConverterManager(@NonNull DBToDTOConverter<DbFrom, DtoFrom> dbToDTOConverter,
                              @NonNull DTOToDTOConverter<DtoFrom, DtoTo> dtoToDTOConverter,
                              @NonNull DTOToDBConverter<DtoTo, DbTo> dtoToDBConverter) {
        this.dbToDTOConverter = dbToDTOConverter;
        this.dtoToDTOConverter = dtoToDTOConverter;
        this.dtoToDBConverter = dtoToDBConverter;
    }

    public void convert(@NonNull DbFrom dbFrom, @NonNull DbTo dbTo) throws ConversionException {
        try {
            var dtoFrom = dbToDTOConverter.convert(dbFrom);
            var dtoTO = dtoToDTOConverter.convert(dtoFrom);
            dtoToDBConverter.convert(dtoTO, dbTo);
        } catch (ConversionException exception) {
            dbFrom.completeConversionWithError();
            dbTo.completeConversionWithError();
            throw exception;
        }
        dbFrom.completeConversionWithSuccess();
        dbTo.completeConversionWithSuccess();
    }
}


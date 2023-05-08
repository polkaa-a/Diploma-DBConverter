package conversion.dtotodatabase;

import database.Database;
import dto.base.DatabaseDTO;
import exceptions.ConversionException;
import lombok.NonNull;

public interface DTOToDBConverter<T extends DatabaseDTO<S>, S extends Database> {
    void convert(@NonNull T databaseDto, @NonNull S database) throws ConversionException;
}

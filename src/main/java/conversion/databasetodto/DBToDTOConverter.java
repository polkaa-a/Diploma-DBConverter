package conversion.databasetodto;

import database.Database;
import dto.base.DatabaseDTO;
import exceptions.ConversionException;
import lombok.NonNull;

public interface DBToDTOConverter<T extends Database, S extends DatabaseDTO<T>> {
    S convert(@NonNull T database) throws ConversionException;
}


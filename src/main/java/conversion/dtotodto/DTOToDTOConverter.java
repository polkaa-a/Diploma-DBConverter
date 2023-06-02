package conversion.dtotodto;

import database.Database;
import dto.base.DatabaseDTO;
import exceptions.ConversionException;
import lombok.NonNull;

public interface
DTOToDTOConverter<T extends DatabaseDTO<? extends Database>, S extends DatabaseDTO<? extends Database>> {
    S convert(@NonNull T databaseDTO) throws ConversionException;
}

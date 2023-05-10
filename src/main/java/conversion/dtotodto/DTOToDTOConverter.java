package conversion.dtotodto;

import database.Database;
import dto.base.DatabaseDTO;
import lombok.NonNull;

public interface
DTOToDTOConverter<T extends DatabaseDTO<? extends Database>, S extends DatabaseDTO<? extends Database>> {
    S convert(@NonNull T databaseDTO);
}

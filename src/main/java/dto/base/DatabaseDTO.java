package dto.base;

import database.Database;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class DatabaseDTO<T extends Database> {

    //name of database
    private final String name;

    public DatabaseDTO(@NonNull String name) {
        this.name = name;
    }
}

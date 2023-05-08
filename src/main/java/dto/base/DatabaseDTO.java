package dto.base;

import database.Database;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class DatabaseDTO<T extends Database> {

    //name of database
    private final String name;
    //null or database, that was mapped on this dto
    private final T database;

    //if dto is made from another dto
    public DatabaseDTO(@NonNull String name) {
        this.name = name;
        database = null;
    }

    //if dto is made from database
    public DatabaseDTO(@NonNull String name, @NonNull T database) {
        this.name = name;
        this.database = database;
    }
}

package database;

import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
public abstract class Database {

    protected final Set<String> names;
    protected final String name;

    Database(@NonNull String dbName, Set<String> names) {
        this.name = dbName;
        this.names = names;
    }

    public abstract void completeConversionWithError();

    public abstract void completeConversionWithSuccess();
}

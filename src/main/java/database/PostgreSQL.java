package database;

import exceptions.ConversionException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static data.ExceptionMessagesProvider.getTablesDataException;
import static data.postgresql.ConstantsProvider.TABLE_NAME_PATTERN;
import static data.postgresql.ConstantsProvider.TABLE_TYPE;

@Slf4j
public class PostgreSQL extends Database {

    @Getter
    protected final DatabaseMetaData databaseMetaData;
    @Getter
    protected final Connection connection;
    protected Set<String> allTablesNames;

    public PostgreSQL(String dbName, Set<String> names, @NonNull Connection connection) throws SQLException {
        super(dbName, names);
        this.connection = connection;
        databaseMetaData = connection.getMetaData();
    }

    public Set<String> getAllTablesNames() throws ConversionException {
        if (allTablesNames == null) {
            allTablesNames = new HashSet<>();
            try {
                var type = new String[]{TABLE_TYPE};
                var rs = databaseMetaData.getTables(null, null, TABLE_NAME_PATTERN, type);
                while (rs.next()) allTablesNames.add(rs.getString(3));
            } catch (SQLException exception) {
                throw new ConversionException(getTablesDataException(name), exception);
            }
        }
        return allTablesNames;
    }

    protected void closeConnection() {
        try {
            connection.close();
        } catch (SQLException exception) {
            log.warn("Connection isn't closed (database access error occurs): " + exception.getMessage());
        }
    }

    @Override
    public void completeConversionWithError() {
        closeConnection();
    }

    @Override
    public void completeConversionWithSuccess() {
        closeConnection();
    }
}

package conversion.databasetodto.relational;

import conversion.databasetodto.DBToDTOConverter;
import conversion.types.PostgresSQLTypesConverter;
import database.PostgreSQL;
import dto.postgresql.PostgreSQLDatabaseDTO;
import dto.postgresql.PostgreSQLFieldDTO;
import dto.postgresql.PostgreSQLForeignKeyDTO;
import dto.postgresql.PostgreSQLTableDTO;
import exceptions.ConversionException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static data.postgresql.ConstantsProvider.*;
import static data.postgresql.ExceptionMessagesProvider.*;
import static data.postgresql.QueryGenerator.getColumnsDataQuery;

//DTOPostgreSQLConverter converts PostgreSQL to PostgreSQLDatabaseDTO
public class PostgreSQLToDTOConverter implements DBToDTOConverter<PostgreSQL, PostgreSQLDatabaseDTO> {

    private Set<PostgreSQLTableDTO> tablesDTO;
    private PostgreSQL postgreSQL;

    @Override
    public PostgreSQLDatabaseDTO convert(PostgreSQL postgreSQL) throws ConversionException {
        this.postgreSQL = postgreSQL;
        tablesDTO = new HashSet<>();
        //make from each table PostgreSQLTableDTO
        for (var tableName : postgreSQL.getNames()) addTable(tableName);
        return new PostgreSQLDatabaseDTO(postgreSQL.getName(), tablesDTO);
    }

    private void addTable(String name) throws ConversionException {
        if (postgreSQL.getAllTablesNames().contains(name)) {
            var tableDTO = new PostgreSQLTableDTO(name);
            makeFieldsDTOsFromTable(tableDTO);
            tablesDTO.add(tableDTO);
        }
    }

    private void makeFieldsDTOsFromTable(PostgreSQLTableDTO tableDTO) throws ConversionException {
        var tableName = tableDTO.getOriginalName();
        try {
            var statement = postgreSQL.getConnection().createStatement();
            var resultSet = statement.executeQuery(getColumnsDataQuery(tableName));
            while (resultSet.next()) {
                var columnName = resultSet.getString(COLUMN_NAME);
                var dataType = resultSet.getString(DATA_TYPE);
                var type = PostgresSQLTypesConverter.getType(dataType);
                var isPK = isPrimary(columnName, tableName);
                var foreignKeyDTO = getFK(columnName, tableName);
                var fieldDTO = new PostgreSQLFieldDTO(columnName, type, isPK, foreignKeyDTO, tableDTO);
                tableDTO.addField(fieldDTO);
            }
        } catch (SQLException exception) {
            throw new ConversionException(getMetadataExceptionMessage(tableName), exception);
        }
    }

    private PostgreSQLForeignKeyDTO getFK(String columnName, String tableName) throws ConversionException {
        try {
            var rs = postgreSQL.getDatabaseMetaData().getImportedKeys(null, null, tableName);
            while (rs.next()) {
                if (columnName.equals(rs.getString(FKCOLUMN_NAME))) {
                    return new PostgreSQLForeignKeyDTO(rs.getString(PKTABLE_NAME), rs.getString(PKCOLUMN_NAME));
                }
            }
        } catch (SQLException exception) {
            throw new ConversionException(getImportedKeysExceptionMessage(tableName), exception);
        }
        return null;
    }

    private boolean isPrimary(String columnName, String tableName) throws ConversionException {
        for (var key : getTablePrimaryKeys(tableName)) {
            if (columnName.equals(key)) return true;
        }
        return false;
    }

    private ArrayList<String> getTablePrimaryKeys(String tableName) throws ConversionException {
        var primaryKeys = new ArrayList<String>();
        try {
            var rs = postgreSQL.getDatabaseMetaData().getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                primaryKeys.add(rs.getString(4));
            }
        } catch (SQLException exception) {
            throw new ConversionException(getPrimaryKeysExceptionMessage(tableName), exception);
        }
        return primaryKeys;
    }
}

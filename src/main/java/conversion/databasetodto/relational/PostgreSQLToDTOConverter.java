package conversion.databasetodto.relational;

import conversion.databasetodto.DBToDTOConverter;
import conversion.types.PostgresSQLTypesConverter;
import database.PostgreSQL;
import dto.postgresql.PostgreSQLDatabaseDTO;
import dto.postgresql.PostgreSQLFieldDTO;
import dto.postgresql.PostgreSQLForeignKeyDTO;
import dto.postgresql.PostgreSQLTableDTO;
import exceptions.ConversionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static data.ExceptionMessagesProvider.*;
import static data.postgresql.ConstantsProvider.*;
import static data.postgresql.QueryGenerator.getColumnsDataQuery;

//DTOPostgreSQLConverter converts PostgreSQL to PostgreSQLDatabaseDTO
public class PostgreSQLToDTOConverter implements DBToDTOConverter<PostgreSQL, PostgreSQLDatabaseDTO> {

    protected Set<PostgreSQLTableDTO> tablesDTO;
    protected PostgreSQL postgreSQL;

    @Override
    public PostgreSQLDatabaseDTO convert(PostgreSQL postgreSQL) throws ConversionException {
        this.postgreSQL = postgreSQL;
        tablesDTO = new HashSet<>();
        //make from each table PostgreSQLTableDTO
        for (var tableName : postgreSQL.getNames()) addTable(tableName);
        return new PostgreSQLDatabaseDTO(postgreSQL.getName(), tablesDTO);
    }

    protected void addTable(String name) throws ConversionException {
        if (postgreSQL.getAllTablesNames().contains(name)) {
            var tableDTO = new PostgreSQLTableDTO(name);
            makeFieldsDTOsFromTable(tableDTO);
            addForeignKeysDTOs(tableDTO);
            tablesDTO.add(tableDTO);
        }
    }

    protected void makeFieldsDTOsFromTable(PostgreSQLTableDTO tableDTO) throws ConversionException {
        var tableName = tableDTO.getOriginalName();
        try {
            var statement = postgreSQL.getConnection().createStatement();
            var resultSet = statement.executeQuery(getColumnsDataQuery(tableName));
            while (resultSet.next()) {
                var columnName = resultSet.getString(COLUMN_NAME);
                var dataType = resultSet.getString(DATA_TYPE);
                var type = PostgresSQLTypesConverter.getType(dataType);
                var isPK = isPrimary(columnName, tableName);
                new PostgreSQLFieldDTO(columnName, type, isPK, false, tableDTO);
            }
        } catch (SQLException exception) {
            throw new ConversionException(getMetadataExceptionMessage(tableName), exception);
        }
    }

    protected PostgreSQLForeignKeyDTO
    createFK(PostgreSQLFieldDTO fieldDTO, String tableName) throws ConversionException {
        try {
            var rs = postgreSQL.getDatabaseMetaData().getImportedKeys(null, null, tableName);
            while (rs.next()) {
                if (fieldDTO.getOriginalName().equals(rs.getString(FKCOLUMN_NAME))) {
                    for (var table : tablesDTO) {
                        if (table.getOriginalName().equals(rs.getString(PKTABLE_NAME))) {
                            return initFK(table, rs, fieldDTO);
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            throw new ConversionException(getImportedKeysExceptionMessage(tableName), exception);
        }
        return null;
    }

    protected PostgreSQLForeignKeyDTO
    initFK(PostgreSQLTableDTO table, ResultSet rs, PostgreSQLFieldDTO fieldDTO) throws SQLException {
        for (var field : table.getFields()) {
            if (field.getOriginalName().equals(rs.getString(PKCOLUMN_NAME))) {
                var fk = new PostgreSQLForeignKeyDTO();
                fk.setRelField(field);
                fk.setCurField(fieldDTO);
                fieldDTO.setFK(true);
                return fk;
            }
        }
        return null;
    }

    protected void addForeignKeysDTOs(PostgreSQLTableDTO tableDTO) throws ConversionException {
        for (var field : tableDTO.getFields()) {
            createFK(field, tableDTO.getOriginalName());
        }
    }

    protected boolean isPrimary(String columnName, String tableName) throws ConversionException {
        for (var key : getTablePrimaryKeys(tableName)) {
            if (columnName.equals(key)) return true;
        }
        return false;
    }

    protected ArrayList<String> getTablePrimaryKeys(String tableName) throws ConversionException {
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

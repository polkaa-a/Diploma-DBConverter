package conversion.dtotodatabase.relational;

import conversion.dtotodatabase.DTOToDBConverter;
import conversion.types.PostgresSQLTypesConverter;
import database.PostgreSQL;
import dto.postgresql.PostgreSQLDatabaseDTO;
import dto.postgresql.PostgreSQLFieldDTO;
import exceptions.ConversionException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonType;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import static data.ExceptionMessagesProvider.*;
import static data.postgresql.QueryGenerator.*;

@Slf4j
//PostgreSQLConverter converts PostgreSQLDatabaseDTO to PostgreSQL
public class DTOToPostgreSQLConverter implements DTOToDBConverter<PostgreSQLDatabaseDTO, PostgreSQL> {

    protected PostgreSQLDatabaseDTO postgreSQLDatabaseDTO;
    protected PostgreSQL postgreSQL;

    @Override
    public final void convert(@NonNull PostgreSQLDatabaseDTO postgreSQLDatabaseDTO,
                              @NonNull PostgreSQL postgreSQL) throws ConversionException {
        this.postgreSQL = postgreSQL;
        this.postgreSQLDatabaseDTO = postgreSQLDatabaseDTO;
        createTables();
        fillAllData();
        fillFKColumns();
        createFK();
    }

    protected void createTables() throws ConversionException {
        for (var tableDTO : postgreSQLDatabaseDTO.getTables()) {
            try {
                var query = generateSQLCreateTable(tableDTO);
                postgreSQL.getConnection().createStatement().executeUpdate(query);
                log.info("Table was created, table's name: " + tableDTO.getName() + ", query: " + query);
            } catch (SQLException exception) {
                throw new ConversionException(getCreateTableException(tableDTO.getName()), exception);
            }
        }
        log.info("All tables are created");
    }

    protected void fillAllData() throws ConversionException {
        for (var tableDTO : postgreSQLDatabaseDTO.getTables()) {
            for (var source : tableDTO.getSources()) {
                try {
                    var statement = postgreSQL.getConnection().createStatement();
                    var rows = source.getValues();
                    for (var row : rows) {
                        var fields = new LinkedHashMap<String, Object>();
                        row.forEach((fieldDTO, val) -> tableDTO.getFields().forEach(f -> {
                            var sameNames = f.getOriginalName().equals(fieldDTO.getOriginalName());
                            var convertedType = PostgresSQLTypesConverter.convert((BsonType) fieldDTO.getType());
                            var sameTypes = f.getType().equals(convertedType);
                            if (sameNames && sameTypes) fields.put(f.getName(), val);
                        }));
                        var query = getInsertOneRowQuery(tableDTO, fields);
                        statement.executeUpdate(query);
                        log.info("Query was executed: " + query);
                    }
                } catch (SQLException exception) {
                    throw new ConversionException(getFillDataExceptionMessage(tableDTO.getOriginalName()), exception);
                }
            }
            log.info("Data is filled for table: " + tableDTO.getName());
        }
        log.info("All data (except FK) is filled");
    }

    protected void fillFKColumns() throws ConversionException {
        for (var table : postgreSQLDatabaseDTO.getTables()) {
            for (var field : table.getFields().stream().filter(PostgreSQLFieldDTO::isFK).toList()) {
                try {
                    var statement = postgreSQL.getConnection().createStatement();
                    for (var fk : field.getForeignKeys()) {
                        var query = getPKForFKQuery(fk);
                        var rs = statement.executeQuery(query);
                        log.info("Query was executed: " + query);
                        if (rs.next()) {
                            var id = rs.getInt(fk.getRelFieldDTO().getName());
                            var updateFKQuery = getUpdateFKQuery(table.getName(), field.getName(), id, fk);
                            statement.executeUpdate(updateFKQuery);
                            log.info("Query was executed: " + updateFKQuery);
                        }
                    }
                } catch (SQLException exception) {
                    throw new ConversionException("fillFKColumns", exception);
                }
            }
            log.info("FK columns is filled for table: " + table.getName());
        }
        log.info("FK is filled");
    }

    protected void createFK() throws ConversionException {
        for (var tableDTO : postgreSQLDatabaseDTO.getTables()) {
            try {
                postgreSQL.getConnection().createStatement().executeUpdate(generateSQLForeignKeys(tableDTO));
                log.info("FK is created for table: " + tableDTO.getName());
            } catch (SQLException exception) {
                throw new ConversionException(getCreateFKException(tableDTO.getName()), exception);
            }
        }
        log.info("FK refs are created");
    }
}

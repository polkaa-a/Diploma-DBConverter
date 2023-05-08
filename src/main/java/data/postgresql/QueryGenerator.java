package data.postgresql;

import dto.postgresql.PostgreSQLFieldDTO;
import dto.postgresql.PostgreSQLTableDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class QueryGenerator {

    public static String generateSQLCreateTable(PostgreSQLTableDTO table) {
        return "create table if not exists " + "\"" +
                table.getName() + "\"" +
                " ( " +
                generateSQLFields(table.getFields()) +
                "); ";
    }

    private static String generateSQLFields(Set<PostgreSQLFieldDTO> fields) {
        var fieldsString = new StringBuilder();
        var iterator = fields.iterator();
        while (iterator.hasNext()) {
            var fieldDTO = iterator.next();
            fieldsString
                    .append(fieldDTO.getName()).append(" ")
                    .append(fieldDTO.getType().getName())
                    .append(fieldDTO.isPK() ? " primary key" : "");
            if (iterator.hasNext()) {
                fieldsString.append(", ");
            }
        }
        return fieldsString.toString();
    }

    public static String getInsertOneRowQuery(PostgreSQLTableDTO tableDTO, LinkedHashMap<String, Object> fields) {
        return "INSERT INTO " + tableDTO.getName() +
                "(" + getFormattedFieldsNames(fields.keySet()) + ")" +
                " VALUES (" + getFormattedFieldsValues(List.of(fields.values())) + ");";
    }

    public static String generateSQLForeignKeys(PostgreSQLTableDTO table) {
        var addForeignKeysSQL = new StringBuilder();
        for (var field : table.getFields()) {
            if (field.getFK() != null) {
                var fkName = "fk_" + table.getName() + "_" + field.getFK().getRelTableName();
                addForeignKeysSQL
                        .append("alter table ")
                        .append(table.getName())
                        .append(" drop constraint if exists ")
                        .append(fkName)
                        .append(";")
                        .append(" alter table ")
                        .append(table.getName())
                        .append(" add constraint ")
                        .append(fkName)
                        .append(" foreign key (")
                        .append(field.getName())
                        .append(") ")
                        .append("references ")
                        .append(field.getFK().getRelTableName())
                        .append(" (")
                        .append(field.getFK().getRelFieldName())
                        .append("); ");
            }
        }
        return addForeignKeysSQL.toString();
    }

    public static String getFormattedFieldsNames(Set<String> fields) {
        var list = new StringBuilder();
        for (var name : fields) {
            list.append(name).append(", ");
        }
        return list.substring(0, list.length() - 2);
    }

    public static String getFormattedFieldsValues(List<Object> values) {
        var list = new StringBuilder();
        for (var val : values) {
            list.append("'").append(val).append("'").append(", ");
        }
        return list.substring(0, list.length() - 2);
    }

    public static String getColumnsDataQuery(String tableName) {
        return "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = " + tableName;
    }
}

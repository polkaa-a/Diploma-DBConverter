package data.postgresql;

import conversion.types.enums.PostgreSQLTypes;
import dto.postgresql.PostgreSQLFieldDTO;
import dto.postgresql.PostgreSQLForeignKeyDTO;
import dto.postgresql.PostgreSQLTableDTO;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class QueryGenerator {

    public static String generateSQLCreateTable(PostgreSQLTableDTO table) {
        return "create table if not exists " +
                table.getName() +
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
                    .append(((PostgreSQLTypes) fieldDTO.getType()).getName())
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
                " VALUES (" + getFormattedFieldsValues(fields.values()) + ");";
    }

    public static String generateSQLForeignKeys(PostgreSQLTableDTO table) {
        var addForeignKeysSQL = new StringBuilder();
        for (var field : table.getFields()) {
            if (field.isFK()) {
                var fk = field.getForeignKeys().iterator().next();
                var relTableName = ((PostgreSQLTableDTO) fk.getRelFieldDTO().getFieldsKeeperDTO()).getName();
                var fkName = "fk_" + table.getName() + "_" + relTableName;
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
                        .append(relTableName)
                        .append(" (")
                        .append(fk.getRelFieldDTO().getName())
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
        if (fields.isEmpty()) return list.toString();
        else return list.substring(0, list.length() - 2);
    }

    public static String getFormattedFieldsValues(Collection<Object> values) {
        var list = new StringBuilder();
        for (var val : values) {
            list.append("'").append(val).append("'").append(", ");
        }

        if (values.isEmpty()) return list.toString();
        else return list.substring(0, list.length() - 2);
    }

    public static String getColumnsDataQuery(String tableName) {
        return "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = " + tableName;
    }

    public static String getPKForFKQuery(PostgreSQLForeignKeyDTO fk) {
        return "select " + fk.getRelFieldDTO().getName() +
                " from " + ((PostgreSQLTableDTO) fk.getRelFieldDTO().getFieldsKeeperDTO()).getOriginalName() +
                getWhereConditionQuery(fk.getRelValues());
    }

    public static String getUpdateFKQuery(String tableName, String fieldName, int id, PostgreSQLForeignKeyDTO fk) {
        return "update " + tableName + " set " + fieldName + " = " + id + getWhereConditionQuery(fk.getCurValues());
    }


    public static String getWhereConditionQuery(Map<PostgreSQLFieldDTO, Object> values) {
        var query = new StringBuilder(" where ");
        values.forEach((k, v) -> query.append(k.getName()).append(" = '").append(v).append("' AND "));
        if (values.isEmpty()) return query.toString();
        else return query.substring(0, query.length() - 5);
    }
}

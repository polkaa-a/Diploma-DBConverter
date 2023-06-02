package conversion.types;

import conversion.types.enums.PostgreSQLTypes;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonType;

@Slf4j
public class PostgresSQLTypesConverter {

    public static PostgreSQLTypes convert(BsonType bsonType) {
        switch (bsonType) {
            case DOUBLE -> {
                return PostgreSQLTypes.FLOAT8;
            }
            case STRING, ARRAY, SYMBOL, REGULAR_EXPRESSION, OBJECT_ID, JAVASCRIPT, JAVASCRIPT_WITH_SCOPE, DB_POINTER -> {
                return PostgreSQLTypes.TEXT;
            }
            case BINARY -> {
                return PostgreSQLTypes.BYTEA;
            }
            case UNDEFINED, NULL, MIN_KEY, MAX_KEY -> {
                return null;
            }
            case BOOLEAN -> {
                return PostgreSQLTypes.BOOLEAN;
            }
            case DATE_TIME -> {
                return PostgreSQLTypes.DATE;
            }
            case DOCUMENT, INT32 -> {
                return PostgreSQLTypes.INT;
            }
            case TIMESTAMP -> {
                return PostgreSQLTypes.TIMESTAMP;
            }
            case INT64 -> {
                return PostgreSQLTypes.BIGINT;
            }
            case DECIMAL128 -> {
                return PostgreSQLTypes.NUMERIC;
            }
        }
        return null;
    }

    public static PostgreSQLTypes getType(String name) throws IllegalArgumentException {
        for (PostgreSQLTypes type : PostgreSQLTypes.values()) {
            if (type.getName().equals(name)) return type;
        }
        throw new IllegalArgumentException("name must match the type of PostgreSQL");
    }
}

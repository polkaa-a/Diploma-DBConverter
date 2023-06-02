package data;

public class ExceptionMessagesProvider {
    private static final String EXC_METADATA = "Couldn't select data from information_schema.columns for table: ";
    private static final String EXC_IMPORTED_KEYS = "Couldn't get imported keys from table: ";
    private static final String EXC_PRIMARY_KEYS = "Couldn't get primary keys from table: ";
    private static final String EXC_FILL_DATA = "Couldn't get primary keys from table: ";
    private static final String EXC_TABLES_DATA = "Couldn't get tables names from PostgreSQL database: ";
    private static final String EXC_CREATE_TABLE = "Couldn't create table: ";
    private static final String EXC_CREATE_FK = "Couldn't create FK for table: ";

    public static String getMetadataExceptionMessage(String tableName) {
        return EXC_METADATA + tableName;
    }

    public static String getImportedKeysExceptionMessage(String tableName) {
        return EXC_IMPORTED_KEYS + tableName;
    }

    public static String getPrimaryKeysExceptionMessage(String tableName) {
        return EXC_PRIMARY_KEYS + tableName;
    }

    public static String getFillDataExceptionMessage(String tableName) {
        return EXC_FILL_DATA + tableName;
    }

    public static String getTablesDataException(String dbName) {
        return EXC_TABLES_DATA + dbName;
    }

    public static String getCreateTableException(String tableName) {
        return EXC_CREATE_TABLE + tableName;
    }

    public static String getCreateFKException(String tableName) {
        return EXC_CREATE_FK + tableName;
    }

}

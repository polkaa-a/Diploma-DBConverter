package data.postgresql;

public class ExceptionMessagesProvider {
    private static final String EXC_METADATA = "Couldn't select data from information_schema.columns for table: ";
    private static final String EXC_IMPORTED_KEYS = "Couldn't get imported keys from table: ";
    private static final String EXC_PRIMARY_KEYS = "Couldn't get primary keys from table: ";
    private static final String EXC_FILL_DATA = "Couldn't get primary keys from table: ";

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

}

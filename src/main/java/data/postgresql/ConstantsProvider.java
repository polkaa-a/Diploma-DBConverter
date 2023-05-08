package data.postgresql;

import conversion.types.enums.PostgreSQLTypes;

import static conversion.types.enums.PostgreSQLTypes.INT;
import static conversion.types.enums.PostgreSQLTypes.SERIAL;

public class ConstantsProvider {
    public static final String TABLE_NAME_PATTERN = "%";
    public static final String TABLE_TYPE = "TABLE";
    public static final String NAME_DELIMITER = "_";
    public static final String COLUMN_NAME = "column_name";
    public static final String DATA_TYPE = "data_type";
    public static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";
    public static final String PKTABLE_NAME = "PKTABLE_NAME";
    public static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
    public static final String ID_COLUMN_NAME = "id";
    public static final PostgreSQLTypes PKCOLUMN_TYPE = SERIAL;
    public static final PostgreSQLTypes FKCOLUMN_TYPE = INT;
}

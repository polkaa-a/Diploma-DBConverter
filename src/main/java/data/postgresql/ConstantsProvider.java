package data.postgresql;

import conversion.types.enums.PostgreSQLTypes;
import dto.postgresql.PostgreSQLFieldDTO;

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

    public static String getFKFieldName(String fieldName) {
        return fieldName + NAME_DELIMITER + ID_COLUMN_NAME;
    }

    public static String getOriginalNameFromFK(String fkFieldName) {
        return fkFieldName.replace(NAME_DELIMITER + ID_COLUMN_NAME, "");
    }

    public static String getMarkedFieldName(PostgreSQLFieldDTO fieldDTO) {
        return fieldDTO.getName() + NAME_DELIMITER + ((PostgreSQLTypes) fieldDTO.getType()).getMark();
    }
}

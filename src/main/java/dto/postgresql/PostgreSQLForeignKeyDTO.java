package dto.postgresql;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PostgreSQLForeignKeyDTO {
    protected final Map<PostgreSQLFieldDTO, Object> relValues;
    protected final Map<PostgreSQLFieldDTO, Object> curValues;
    protected PostgreSQLFieldDTO relFieldDTO;
    protected PostgreSQLFieldDTO curFieldDTO;

    public PostgreSQLForeignKeyDTO() {
        relValues = new HashMap<>();
        curValues = new HashMap<>();
    }

    public void setRelValue(@NonNull PostgreSQLFieldDTO fieldDTO, Object value) {
        relValues.put(fieldDTO, value);
    }

    public void setCurValue(@NonNull PostgreSQLFieldDTO fieldDTO, Object value) {
        curValues.put(fieldDTO, value);
    }

    public void setCurField(@NonNull PostgreSQLFieldDTO fieldDTO) {
        curFieldDTO = fieldDTO;
        curFieldDTO.foreignKeys.add(this);
    }

    public void setRelField(@NonNull PostgreSQLFieldDTO fieldDTO) {
        relFieldDTO = fieldDTO;
    }
}

package dto.postgresql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class PostgreSQLForeignKeyDTO {
    @NonNull
    private String relTableName;
    @NonNull
    private String relFieldName;
}

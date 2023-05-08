package dto.postgresql;

import database.PostgreSQL;
import dto.base.DatabaseDTO;
import lombok.Getter;

import java.util.Set;

@Getter
public class PostgreSQLDatabaseDTO extends DatabaseDTO<PostgreSQL> {
    private final Set<PostgreSQLTableDTO> tables;

    public PostgreSQLDatabaseDTO(String name, Set<PostgreSQLTableDTO> tables) {
        super(name);
        this.tables = tables;
    }

    public final boolean setNewTableName(PostgreSQLTableDTO tableDTO, String newName) {
        var isCorrectName = tables.stream().filter(t -> t.getName().equals(newName)).toList().isEmpty();
        if (isCorrectName) {
            tableDTO.setNewName(newName);
            return true;
        } else return false;
    }
}

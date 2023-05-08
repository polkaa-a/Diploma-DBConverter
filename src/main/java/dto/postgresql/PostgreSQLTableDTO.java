package dto.postgresql;

import dto.base.FieldsKeeperDTO;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@Getter
public class PostgreSQLTableDTO extends FieldsKeeperDTO<PostgreSQLFieldDTO> {
    @NonNull
    private final String originalName;
    private String newName;

    public PostgreSQLTableDTO(String name) {
        super();
        this.originalName = name;
    }

    @Override
    //tables are equal if they have the same names
    public boolean equals(Object o) {
        return (o instanceof PostgreSQLTableDTO) && Objects.equals(this.toString(), o.toString());
    }

    protected void setNewName(String newName) {
        this.newName = newName;
    }

    //if table has new name - returns newName, otherwise returns originalName
    public final String getName() {
        return Objects.requireNonNullElseGet(newName, this::getOriginalName);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}

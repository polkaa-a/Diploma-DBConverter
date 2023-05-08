package dto.base;

import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@Getter
public abstract class FieldDTO {
    //name from real database
    protected final String originalName;
    //owner
    protected final FieldsKeeperDTO<? extends FieldDTO> fieldDTOFieldsKeeperDTO;
    //changed name (if it was) or null
    protected String newName;

    public FieldDTO(@NonNull String originalName, FieldsKeeperDTO<? extends FieldDTO> fieldDTOFieldsKeeperDTO) {
        this.originalName = originalName;
        this.fieldDTOFieldsKeeperDTO = fieldDTOFieldsKeeperDTO;
    }

    public final boolean setNewName(String newName) {
        var fields = fieldDTOFieldsKeeperDTO.getFields();
        var isCorrectName = fields.stream().filter(f -> f.getName().equals(newName)).toList().isEmpty();
        if (isCorrectName) {
            this.newName = newName;
            return true;
        } else return false;
    }

    //if field has new name - returns newName, otherwise returns originalName
    public final String getName() {
        return Objects.requireNonNullElseGet(newName, this::getOriginalName);
    }

    //returns field's value
    public abstract Object getValue();

    //by name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldDTO fieldDTO = (FieldDTO) o;

        return getName().equals(fieldDTO.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}

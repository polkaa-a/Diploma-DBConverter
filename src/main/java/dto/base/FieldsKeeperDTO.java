package dto.base;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class FieldsKeeperDTO<T extends FieldDTO> {

    protected final Set<T> fields;

    /*
     * if dto is made from another dto we need source(s)
     * if dto is made from database sources can be empty
     */
    protected final Set<FieldsKeeperDTO<? extends FieldDTO>> sources;

    public FieldsKeeperDTO() {
        fields = new HashSet<>();
        sources = new HashSet<>();
    }

    public boolean addSource(@NonNull FieldsKeeperDTO<? extends FieldDTO> source) {
        return sources.add(source);
    }

    public boolean addField(@NonNull T fieldDTO) {
        return fields.add(fieldDTO);
    }

    //by field's original name
    public final Object getFieldValue(String fieldName) {
        var fieldDTO = fields.stream().filter(f -> f.getOriginalName().equals(fieldName)).findAny();
        return fieldDTO.map(FieldDTO::getValue).orElse(null);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}

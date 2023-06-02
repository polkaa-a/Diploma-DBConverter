package dto.base;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;

@Getter
public abstract class FieldsKeeperDTO<T extends FieldDTO> {

    protected final Set<T> fields;

    /*
     * if dto is made from another dto we need source(s)
     * if dto is made from database sources can be empty
     */
    protected final List<FieldsKeeperDTO<? extends FieldDTO>> sources;

    public FieldsKeeperDTO() {
        fields = new HashSet<>();
        sources = new ArrayList<>();
    }

    public boolean addSource(@NonNull FieldsKeeperDTO<? extends FieldDTO> source) {
        return sources.add(source);
    }

    public boolean addField(@NonNull T fieldDTO) {
        return fields.add(fieldDTO);
    }

    public abstract List<Map<FieldDTO, Object>> getValues();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}

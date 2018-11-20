package io.shooroop.gaga.impl.deserialize.setters;

import io.shooroop.gaga.impl.deserialize.assemblers.ObjectAssemblerImpl;
import io.shooroop.gaga.impl.deserialize.parser.FieldRepresentation;

import java.lang.reflect.Field;

public class ObjectSetter implements FieldSetter<FieldRepresentation> {
    private final ObjectAssemblerImpl objectAssemblerImpl;

    @Override
    public void set(Field field,
                    Object object,
                    FieldRepresentation fieldRepresentation) {
        try {
            field.set(object,
                    objectAssemblerImpl.assembleObjectOf(fieldRepresentation.type,
                            fieldRepresentation.value));
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    ObjectSetter(ObjectAssemblerImpl objectAssemblerImpl) {
        this.objectAssemblerImpl = objectAssemblerImpl;
    }
}

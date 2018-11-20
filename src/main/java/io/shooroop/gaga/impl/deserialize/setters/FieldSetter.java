package io.shooroop.gaga.impl.deserialize.setters;

import java.lang.reflect.Field;

public interface FieldSetter<T> {

    void set(Field field,
             Object object,
             T fieldRepresentation);

}

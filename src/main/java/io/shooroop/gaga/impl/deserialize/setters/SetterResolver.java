package io.shooroop.gaga.impl.deserialize.setters;

public interface SetterResolver<T> {

    FieldSetter<T> resolve(String tag);

}

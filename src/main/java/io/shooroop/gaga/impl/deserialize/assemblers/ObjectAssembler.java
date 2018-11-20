package io.shooroop.gaga.impl.deserialize.assemblers;

public interface ObjectAssembler<T> {

    Object assembleObjectOf(String className, T value);

}

package io.shooroop.gaga.contract;

public interface SerializerToBytes<T> {
    byte[] serializationOf(T t);
}

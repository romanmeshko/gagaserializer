package io.shooroop.gaga.contract;

public interface DeserializerFromBytes<T> {
    T deserializationOf(byte[] data, Class<T> clazz);
}

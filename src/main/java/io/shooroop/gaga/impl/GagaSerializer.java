package io.shooroop.gaga.impl;

import io.shooroop.gaga.contract.SerializerToBytes;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static io.shooroop.gaga.impl.Settings.*;
import static io.shooroop.gaga.impl.Settings.Enum.BEGIN;
import static io.shooroop.gaga.impl.Settings.Enum.END;
import static java.util.Objects.isNull;
import static org.springframework.util.ReflectionUtils.doWithLocalFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public class GagaSerializer implements SerializerToBytes {

    @Override
    public byte[] serializationOf(@NonNull Object object) {
        return serializeObject(object).
                toString().
                getBytes(CHARSET);
    }

    private StringBuilder serializeObject(Object object) {
        StringBuilder buffer = new StringBuilder();
        serializeObject(object.getClass(), object, buffer);
        return buffer;
    }

    private void serializeObject(Class<?> clazz, Object object, StringBuilder buffer) {
        buffer.append(BEGIN_OBJECT);
        serialize(clazz, object, buffer, Optional.empty());
        buffer.append(END_OBJECT);
    }

    private void serialize(Class<?> clazz, Object object,
                           StringBuilder buffer,
                           Optional<ReflectionUtils.FieldFilter> filter) {
        doWithLocalFields(
                clazz,
                (field) -> {
                    if (filter.isPresent() && !filter.get().matches(field)) {
                        return;
                    }
                    makeAccessible(field);
                    Object value = field.get(object);
                    if (isNull(value)) {
                        return;
                    }

                    if (isPrimitiveType(field.getType())) {
                        serializePrimitive(object, buffer, field);
                        return;
                    }

                    if (field.getType().isArray()) {
                        serializeArray(buffer, field, value);
                        return;
                    }

                    if (Collection.class.isAssignableFrom(field.getType())) {
                        serializeCollection(buffer, field, (Collection<?>) value);
                        return;
                    }

                    if (Map.class.isAssignableFrom(field.getType())) {
                        serializeMap(buffer, field, (Map<?, ?>) value);
                        return;
                    }

                    if (field.getType().isEnum()) {
                        serializeEnum(buffer, field, value);
                        return;
                    }

                    markTypeAndName(field.getGenericType().getTypeName(), field.getName(), buffer);
                    serializeObject(field.getType(), value, buffer);
                    buffer.append(DELIMITER);
                }

        );
    }

    private void serializePrimitive(Object object, StringBuilder buffer, Field field) throws IllegalAccessException {
        markTypeAndName(field.getType().getTypeName(), field.getName(), buffer).
                append(NAME_VALUE_DELIMIT).
                append(field.get(object).toString()).
                append(DELIMITER);
    }

    private void serializeArray(StringBuilder buffer, Field field, Object value) {
        markTypeAndName(field.getType().getTypeName(), field.getName(), buffer).
                append(COLLECT_BEGIN);
        for (int i = 0; i < Array.getLength(value); i++) {
            serializeElement(Array.get(value, i), buffer);
            buffer.append(COLLECT_DELIMIT);
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(COLLECT_END);
        buffer.append(DELIMITER);
    }

    private void serializeElement(Object element, StringBuilder buffer) {
        if (isPrimitiveType(element.getClass())) {
            buffer.append(element.toString());
        } else {
            serializeObject(element.getClass(), element, buffer);
        }
    }

    private void serializeCollection(StringBuilder buffer, Field field, Collection<?> collection) {
        markTypeAndName(field.getGenericType().getTypeName(), field.getName(), buffer).
                append(COLLECT_BEGIN);
        collection.forEach(element -> {
                    serializeElement(element, buffer);
                    buffer.append(COLLECT_DELIMIT);
                }
        );
        buffer.append(COLLECT_END);
        buffer.append(DELIMITER);
    }

    private void serializeEnum(StringBuilder buffer, Field field, Object value) {
        buffer.append(BEGIN);
        buffer.append(field.getName());
        buffer.append(value);
        buffer.append(DELIMITER);
        serialize(value.getClass(),
                value,
                buffer,
                Optional.of((e) -> !e.isEnumConstant() && !e.getName().contains("VALUES"))
        );
        buffer.append(END);
        buffer.append(DELIMITER);
    }

    private void serializeMap(StringBuilder buffer, Field field, Map<?, ?> map) {
        markTypeAndName(field.getGenericType().getTypeName(), field.getName(), buffer).
                append(COLLECT_BEGIN);
        map.forEach((k, v) -> {
                    serializeElement(k, buffer);
                    buffer.append(MAP_DELIMIT);
                    serializeElement(v, buffer);
                    buffer.append(COLLECT_DELIMIT);
                }
        );
        buffer.append(COLLECT_END);
        buffer.append(DELIMITER);
    }


    private StringBuilder markTypeAndName(String typeName, String fieldName, StringBuilder buffer) {
        return buffer.append(typeName).
                append(TYPE_NAME_DELIMIT).
                append(fieldName);
    }

    private boolean isPrimitiveType(Class<?> type) {
        return (type.isPrimitive() && type != void.class) ||
                type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }

}

package io.shooroop.gaga.impl.serialize;

import io.shooroop.gaga.contract.SerializerToBytes;
import io.shooroop.gaga.impl.deserialize.parser.TypeTag;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static io.shooroop.gaga.impl.GagaUtils.isPrimitiveType;
import static io.shooroop.gaga.impl.Settings.*;
import static io.shooroop.gaga.impl.deserialize.parser.FieldRepresentation.presentationOf;
import static java.util.Objects.isNull;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ReflectionUtils.doWithLocalFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public class GagaSerializer<T> implements SerializerToBytes<T> {

    @Override
    public byte[] serializationOf(@NonNull Object object) {
        return serializeObject(object).
                toString().
                getBytes(CHARSET);
    }

    private StringBuilder serializeObject(Object object) {
        notNull(object, "Object is null!");
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
                    if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                            (filter.isPresent() && !filter.get().matches(field))) {
                        return;
                    }
                    makeAccessible(field);
                    Object value = field.get(object);
                    if (isNull(value) || field.getType().isEnum()) {
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

                    buffer.append(
                            presentationOf(TypeTag.OBJECT, field.getGenericType().getTypeName(), field.getName())
                    );
                    serializeObject(field.getType(), value, buffer);
                    buffer.append(DELIMITER);
                }

        );
    }

    private void serializePrimitive(Object object, StringBuilder buffer, Field field) throws IllegalAccessException {
        buffer.append(
                presentationOf(TypeTag.PRIMITIVE, field.getType().getTypeName(), field.getName())).
                append(field.get(object).toString()).
                append(DELIMITER);
    }

    private void serializeArray(StringBuilder buffer, Field field, Object value) {
        buffer.append(
                presentationOf(TypeTag.ARRAY, field.getType().getTypeName(), field.getName())).
                append(COLLECT_BEGIN);
        for (int i = 0; i < Array.getLength(value); i++) {
            serializeElement(Array.get(value, i), buffer);
            buffer.append(COLLECT_DELIMIT);
        }
        collectionEnd(buffer);
    }

    private void collectionEnd(StringBuilder buffer) {
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(TypeTag.COLLECTION.endMark);
    }

    private void serializeElement(Object element, StringBuilder buffer) {
        if (isPrimitiveType(element.getClass())) {
            buffer.append(element.toString());
        } else {
            serializeObject(element.getClass(), element, buffer);
        }
    }

    private void serializeCollection(StringBuilder buffer, Field field, Collection<?> collection) {
        buffer.append(
                presentationOf(TypeTag.COLLECTION, field.getGenericType().getTypeName(), field.getName())).
                append(COLLECT_BEGIN);
        collection.forEach(element -> {
                    serializeElement(element, buffer);
                    buffer.append(COLLECT_DELIMIT);
                }
        );
        collectionEnd(buffer);
    }

    private void serializeMap(StringBuilder buffer, Field field, Map<?, ?> map) {
        buffer.append(
                presentationOf(TypeTag.MAP, field.getGenericType().getTypeName(), field.getName())).
                append(COLLECT_BEGIN);
        map.forEach((k, v) -> {
                    serializeElement(k, buffer);
                    buffer.append(MAP_DELIMIT);
                    serializeElement(v, buffer);
                    buffer.append(COLLECT_DELIMIT);
                }
        );
        collectionEnd(buffer);
    }


}

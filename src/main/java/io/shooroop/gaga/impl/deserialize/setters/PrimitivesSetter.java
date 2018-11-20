package io.shooroop.gaga.impl.deserialize.setters;

import io.shooroop.gaga.impl.deserialize.parser.FieldRepresentation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PrimitivesSetter implements FieldSetter<FieldRepresentation> {
    private Map<String, ThreeConsumerThrowable<Field, Object, String>> typesAssembler;

    public PrimitivesSetter() {
        typesAssembler = new HashMap<>();
        typesAssembler.put("boolean", (field, obj, strVal) -> field.setBoolean(obj, Boolean.parseBoolean(strVal)));
        typesAssembler.put("java.lang.Boolean", (field, obj, strVal) -> field.set(obj, Boolean.parseBoolean(strVal)));
        typesAssembler.put("byte", (field, obj, strVal) -> field.setByte(obj, Byte.parseByte(strVal)));
        typesAssembler.put("java.lang.Byte", (field, obj, strVal) -> field.set(obj, Byte.parseByte(strVal)));
        typesAssembler.put("char", (field, obj, strVal) -> field.setChar(obj, strVal.charAt(0)));
        typesAssembler.put("java.lang.Character", (field, obj, strVal) -> field.set(obj, strVal.charAt(0)));
        typesAssembler.put("short", (field, obj, strVal) -> field.setShort(obj, Short.parseShort(strVal)));
        typesAssembler.put("java.lang.Short", (field, obj, strVal) -> field.set(obj, Short.parseShort(strVal)));
        typesAssembler.put("int", (field, obj, strVal) -> field.setInt(obj, Integer.parseInt(strVal)));
        typesAssembler.put("java.lang.Integer", (field, obj, strVal) -> field.set(obj, Integer.parseInt(strVal)));
        typesAssembler.put("long", (field, obj, strVal) -> field.setLong(obj, (long) Long.parseLong(strVal)));
        typesAssembler.put("java.lang.Long", (field, obj, strVal) -> field.set(obj, (long) Long.parseLong(strVal)));
        typesAssembler.put("float", (field, obj, strVal) -> field.setFloat(obj, Float.parseFloat(strVal)));
        typesAssembler.put("java.lang.Float", (field, obj, strVal) -> field.set(obj, Float.parseFloat(strVal)));
        typesAssembler.put("double", (field, obj, strVal) -> field.setDouble(obj, Double.parseDouble(strVal)));
        typesAssembler.put("java.lang.Double", (field, obj, strVal) -> field.set(obj, Double.parseDouble(strVal)));
        typesAssembler.put("java.lang.String", Field::set);
    }

    @Override
    public void set(Field field,
                    Object object,
                    FieldRepresentation fieldRepresentation) {

        try {
            typesAssembler.
                    get(fieldRepresentation.type).
                    accept(field, object, fieldRepresentation.value);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

}

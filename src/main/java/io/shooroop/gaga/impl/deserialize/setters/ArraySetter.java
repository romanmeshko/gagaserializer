package io.shooroop.gaga.impl.deserialize.setters;

import io.shooroop.gaga.impl.Settings;
import io.shooroop.gaga.impl.deserialize.assemblers.ObjectAssemblerImpl;
import io.shooroop.gaga.impl.deserialize.parser.FieldRepresentation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static io.shooroop.gaga.impl.GagaUtils.getClassByName;
import static io.shooroop.gaga.impl.GagaUtils.isPrimitiveType;

public class ArraySetter implements FieldSetter<FieldRepresentation> {
    private Map<String,
            ThreeConsumerThrowable<Object, Integer, String>> arraysAssembler;
    private final ObjectAssemblerImpl objectAssemblerImpl;

    ArraySetter(ObjectAssemblerImpl objectAssemblerImpl) {
        arraysAssembler = new HashMap<>();
        arraysAssembler.put("boolean", (obj, i, strVal) -> Array.setBoolean(obj, i, Boolean.parseBoolean(strVal)));
        arraysAssembler.put("java.lang.Boolean", (obj, i, strVal) -> Array.setBoolean(obj, i, Boolean.parseBoolean(strVal)));
        arraysAssembler.put("byte", (obj, i, strVal) -> Array.setByte(obj, i, Byte.parseByte(strVal)));
        arraysAssembler.put("java.lang.Byte", (obj, i, strVal) -> Array.setByte(obj, i, Byte.parseByte(strVal)));
        arraysAssembler.put("char", (obj, i, strVal) -> Array.setChar(obj, i, strVal.charAt(0)));
        arraysAssembler.put("java.lang.Character", (obj, i, strVal) -> Array.setChar(obj, i, strVal.charAt(0)));
        arraysAssembler.put("short", (obj, i, strVal) -> Array.setShort(obj, i, Short.parseShort(strVal)));
        arraysAssembler.put("java.lang.Short", (obj, i, strVal) -> Array.setShort(obj, i, Short.parseShort(strVal)));
        arraysAssembler.put("int", (obj, i, strVal) -> Array.setInt(obj, i, Integer.parseInt(strVal)));
        arraysAssembler.put("java.lang.Integer", (obj, i, strVal) -> Array.setInt(obj, i, Integer.parseInt(strVal)));
        arraysAssembler.put("long", (obj, i, strVal) -> Array.setLong(obj, i, Long.parseLong(strVal)));
        arraysAssembler.put("java.lang.Long", (obj, i, strVal) -> Array.setLong(obj, i, Long.parseLong(strVal)));
        arraysAssembler.put("float", (obj, i, strVal) -> Array.setFloat(obj, i, Float.parseFloat(strVal)));
        arraysAssembler.put("java.lang.Float", (obj, i, strVal) -> Array.setFloat(obj, i, Float.parseFloat(strVal)));
        arraysAssembler.put("double", (obj, i, strVal) -> Array.setDouble(obj, i, Double.parseDouble(strVal)));
        arraysAssembler.put("java.lang.Double", (obj, i, strVal) -> Array.setDouble(obj, i, Double.parseDouble(strVal)));
        arraysAssembler.put("java.lang.String", Array::set);
        this.objectAssemblerImpl = objectAssemblerImpl;
    }


    @Override
    public void set(Field field,
                    Object object,
                    FieldRepresentation fieldRepresentation) {

        String typeName = fieldRepresentation.type.substring(0, fieldRepresentation.type.length() - 2);
        Class<?> c = getClassByName(typeName);
        String[] splited = fieldRepresentation.value.
                substring(1, fieldRepresentation.value.length() - 1).
                split(Settings.COLLECT_DELIMIT);
        if (splited.length == 0) {
            return;
        }

        Object array = Array.newInstance(c, splited.length);

        if (isPrimitiveType(c)) {
            collectPrimitives(typeName, splited, array);
        } else {
            collectObjects(typeName, splited, array);
        }

        try {
            field.set(object, array);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void collectObjects(String typeName, String[] splited, Object array) {
        int i = 0;
        for (String s : splited) {
            Array.set(
                    array,
                    i,
                    objectAssemblerImpl.assembleObjectOf(
                            typeName,
                            s
                    )
            );
            i++;
        }
    }

    private void collectPrimitives(String typeName, String[] splited, Object array) {
        final ThreeConsumerThrowable<Object, Integer, String> asm = arraysAssembler.get(typeName);
        int i = 0;
        for (String s : splited) {
            try {
                asm.accept(array, i, s);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
            i++;
        }
    }

}

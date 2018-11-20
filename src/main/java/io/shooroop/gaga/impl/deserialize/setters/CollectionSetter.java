package io.shooroop.gaga.impl.deserialize.setters;

import io.shooroop.gaga.impl.Settings;
import io.shooroop.gaga.impl.deserialize.assemblers.ObjectAssemblerImpl;
import io.shooroop.gaga.impl.deserialize.parser.FieldRepresentation;
import io.shooroop.gaga.impl.deserialize.parser.Parser;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.shooroop.gaga.impl.GagaUtils.*;
import static org.springframework.util.Assert.notNull;

public class CollectionSetter<T> implements FieldSetter<FieldRepresentation> {
    private final Parser<String, FieldRepresentation> parser;
    private final ObjectAssemblerImpl objectAssemblerImpl;

    @SuppressWarnings("unchecked")
    @Override
    public void set(Field field,
                    Object object,
                    FieldRepresentation fieldRepresentation) {

        String[] splited = fieldRepresentation.value.substring(1, fieldRepresentation.value.length() - 1).
                split(Settings.COLLECT_DELIMIT);
        if (splited.length == 0) {
            return;
        }

        String genericClassName = getGenericClass(fieldRepresentation.type);
        if (genericClassName == null) {
            return;
        }

        Collection<T> collection = instantiate(
                (Class<? extends Collection<T>>) getClassByName(fieldRepresentation.type)
        );

        if (isPrimitiveType(getClassByName(genericClassName))) {
            populatePrimitives(splited, genericClassName, collection);
        } else {
            populateObjects(splited, collection);
        }

        try {
            field.set(object, collection);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void populatePrimitives(String[] splited,
                                    String genericClassName,
                                    Collection<T> collection) {
        for (String s : splited) {
            collection.add(
                    (T) objectAssemblerImpl.assembleObjectOf(
                            genericClassName,
                            s
                    )
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void populateObjects(String[] splited,
                                 Collection<T> collection) {
        Map<String, FieldRepresentation> collectionMap = new LinkedHashMap<>();
        for (String s : splited) {
            collectionMap.putAll(
                    parser.parse(s)
            );
        }

        for (Map.Entry<String, FieldRepresentation> entry : collectionMap.entrySet()) {
            FieldRepresentation v = entry.getValue();
            collection.add(
                    (T) objectAssemblerImpl.assembleObjectOf(
                            v.type,
                            v.value
                    )
            );
        }
    }

    CollectionSetter(Parser<String, FieldRepresentation> parser,
                     ObjectAssemblerImpl objectAssemblerImpl) {
        notNull(parser, "parser is null!");
        notNull(objectAssemblerImpl, "objectAssemblerImpl is null!");
        this.parser = parser;
        this.objectAssemblerImpl = objectAssemblerImpl;
    }

}

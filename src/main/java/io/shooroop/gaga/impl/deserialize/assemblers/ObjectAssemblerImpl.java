package io.shooroop.gaga.impl.deserialize.assemblers;

import io.shooroop.gaga.impl.deserialize.parser.FieldRepresentation;
import io.shooroop.gaga.impl.deserialize.parser.Parser;
import io.shooroop.gaga.impl.deserialize.parser.ParserToFieldRepresentation;
import io.shooroop.gaga.impl.deserialize.setters.FieldSetter;
import io.shooroop.gaga.impl.deserialize.setters.SetterResolver;
import io.shooroop.gaga.impl.deserialize.setters.SetterResolverImpl;

import java.util.Map;

import static io.shooroop.gaga.impl.GagaUtils.*;
import static org.springframework.util.ReflectionUtils.doWithLocalFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public class ObjectAssemblerImpl implements ObjectAssembler<String> {
    private final Parser<String, FieldRepresentation> parser;
    private final SetterResolver<FieldRepresentation> setterResolver;
    private final ObjectAssembler<String> primitivesAssembler;

    @Override
    public Object assembleObjectOf(String className, String fieldStringValue) {

        Class<?> clazz = getClassByName(className);
        if (isPrimitiveType(clazz)) {
            return primitivesAssembler.assembleObjectOf(className, fieldStringValue);
        }
        return assembleObjectOf(clazz, fieldStringValue);
    }

    private Object assembleObjectOf(Class<?> clazz, String fieldStringValue) {

        Object object = instantiate(clazz);
        Map<String, FieldRepresentation> namedFields = parser.parse(fieldStringValue);

        doWithLocalFields(clazz, (field) -> {
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                makeAccessible(field);
                FieldRepresentation fieldRepresentation = namedFields.get(field.getName());
                if (fieldRepresentation != null) {
                    FieldSetter<FieldRepresentation> assembler
                            = setterResolver.resolve(fieldRepresentation.tag);
                    assembler.set(field, object, fieldRepresentation);
                }
            }
        });

        return object;
    }


    public ObjectAssemblerImpl() {
        this.parser = new ParserToFieldRepresentation();
        this.setterResolver = new SetterResolverImpl(this, parser);
        this.primitivesAssembler = new PrimitiveAssembler();
    }

}

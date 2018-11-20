package io.shooroop.gaga.impl.deserialize.setters;

import io.shooroop.gaga.impl.deserialize.assemblers.ObjectAssemblerImpl;
import io.shooroop.gaga.impl.deserialize.parser.FieldRepresentation;
import io.shooroop.gaga.impl.deserialize.parser.Parser;
import io.shooroop.gaga.impl.deserialize.parser.TypeTag;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

public class SetterResolverImpl implements SetterResolver<FieldRepresentation> {
    private final Map<TypeTag, FieldSetter<FieldRepresentation>> setters;

    @Override
    public FieldSetter<FieldRepresentation> resolve(String tag) {
        notNull(tag, "tag is null!");
        return setters.get(TypeTag.of(tag));
    }

    @SuppressWarnings("unchecked")
    public SetterResolverImpl(ObjectAssemblerImpl objectAssemblerImpl,
                              Parser<String, FieldRepresentation> parser) {
        notNull(objectAssemblerImpl, "objectAssemblerImpl is null!");
        setters = new HashMap<>();
        setters.put(TypeTag.PRIMITIVE, new PrimitivesSetter());
        setters.put(TypeTag.ARRAY, new ArraySetter(objectAssemblerImpl));
        setters.put(TypeTag.OBJECT, new ObjectSetter(objectAssemblerImpl));
        setters.put(TypeTag.COLLECTION, new CollectionSetter(parser, objectAssemblerImpl));
    }

}

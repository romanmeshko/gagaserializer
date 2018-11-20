package io.shooroop.gaga.impl.deserialize;

import io.shooroop.gaga.contract.DeserializerFromBytes;
import io.shooroop.gaga.impl.Settings;
import io.shooroop.gaga.impl.deserialize.assemblers.ObjectAssembler;
import io.shooroop.gaga.impl.deserialize.assemblers.ObjectAssemblerImpl;

import static org.springframework.util.Assert.notNull;

public class GagaDeserializer<T> implements DeserializerFromBytes<T> {

    private final ObjectAssembler<String> objectAssembler;

    @SuppressWarnings("unchecked")
    @Override
    public T deserializationOf(byte[] data, Class<T> clazz) {
        notNull(data, "data is null!");
        String strObject = new String(data, Settings.CHARSET);
        return (T) objectAssembler.assembleObjectOf(clazz.getName(), strObject);
    }

    public GagaDeserializer() {
        this.objectAssembler = new ObjectAssemblerImpl();
    }
}

import data.TestClass;
import io.shooroop.gaga.contract.DeserializerFromBytes;
import io.shooroop.gaga.contract.SerializerToBytes;
import io.shooroop.gaga.impl.deserialize.GagaDeserializer;
import io.shooroop.gaga.impl.serialize.GagaSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DeserializationTest {
    private final SerializerToBytes<TestClass> gagaSerializer = new GagaSerializer<>();
    private final DeserializerFromBytes<TestClass> deserializer = new GagaDeserializer<>();

    @Test
    public void testLucky() {
        TestClass initial = TestClass.newInstance();
        assertEquals(
                deserializer.deserializationOf(
                        gagaSerializer.serializationOf(initial),
                        TestClass.class
                ),
                initial);

    }

    @Test
    public void testUnlucky() {
        TestClass initial = TestClass.newInstance();
        assertNotEquals(
                deserializer.deserializationOf(
                        gagaSerializer.serializationOf(initial),
                        TestClass.class
                ),
                new TestClass());

    }
}

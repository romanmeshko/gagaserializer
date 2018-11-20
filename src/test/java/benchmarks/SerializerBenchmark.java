package benchmarks;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.TestClass;
import io.shooroop.gaga.contract.SerializerToBytes;
import io.shooroop.gaga.impl.serialize.GagaSerializer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.ByteArrayOutputStream;

@BenchmarkMode(Mode.All)
@State(Scope.Benchmark)
public class SerializerBenchmark {

    private static final TestClass testData = TestClass.newInstance();
    private static final SerializerToBytes<TestClass> gagaSerializer = new GagaSerializer<>();
    private static final ObjectMapper jacksonObjectMapper = new ObjectMapper(new JsonFactory());

    final KryoPool KRYO_POOL = new KryoPool.Builder(
            () -> {
                Kryo kryo = new Kryo();
                kryo.register(TestClass.class);
                return kryo;
            }
    )
            .softReferences()
            .build();


    @Benchmark
    public void testGagaSerializer() {
        gagaSerializer.serializationOf(testData);
    }

    @Benchmark
    public void testKryoSerializer() {
        Kryo kryo = KRYO_POOL.borrow();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            kryo.writeObject(new Output(baos), testData);
            baos.toByteArray();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            KRYO_POOL.release(kryo);
        }
    }

    @Benchmark
    public void testJacksonSerializer() {
        try {
            jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws RunnerException {

        Options opts = new OptionsBuilder()
                .include(benchmarks.SerializerBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .jvmArgs("-server")
                .forks(1)
                .resultFormat(ResultFormatType.TEXT)
                .build();

        new Runner(opts).run();
    }

}

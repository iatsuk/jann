package net.iatsuk.jaan.bench.annoy;

import com.spotify.annoy.ANNIndex;
import com.spotify.annoy.AnnoyIndex;
import com.spotify.annoy.IndexType;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"})
@Warmup(iterations = 3)
@Measurement(iterations = 8)
public class BenchmarkAnnoyScanByDim {

    @Param({"1000"})
    private int N;
    @Param({"100"})
    private int NEIGHBOURS;

    private AnnoyIndex indexAngular25;
    private AnnoyIndex indexAngular50;
    private AnnoyIndex indexAngular75;
    private AnnoyIndex indexAngular100;
    private List<float[]> queries;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkAnnoyScanByDim.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() throws IOException {
        indexAngular25 = new ANNIndex(25, "data/groups_angular_t100_d25.ann", IndexType.ANGULAR);
        indexAngular50 = new ANNIndex(50, "data/groups_angular_t100_d50.ann", IndexType.ANGULAR);
        indexAngular75 = new ANNIndex(75, "data/groups_angular_t100_d75.ann", IndexType.ANGULAR);
        indexAngular100 = new ANNIndex(100, "data/groups_angular_t100_d100.ann", IndexType.ANGULAR);
        Random rnd = new Random(0xDEADBEEF);
        queries = IntStream.range(0, N).boxed()
                .map(i -> makeRandomVector(rnd, 100))
                .collect(Collectors.toList());
    }

    private float[] makeRandomVector(Random rnd, int dim) {
        float[] result = new float[dim];
        for (int i = 0; i < dim; i++) {
            result[i] = rnd.nextFloat() * 2 - 1;
        }
        return result;
    }

    @Benchmark
    public void scanAngularByDim25(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexAngular25.getNearest(Arrays.copyOf(query, 25), NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByDim50(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexAngular50.getNearest(Arrays.copyOf(query, 50), NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByDim75(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexAngular75.getNearest(Arrays.copyOf(query, 75), NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByDim100(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexAngular100.getNearest(Arrays.copyOf(query, 100), NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

}

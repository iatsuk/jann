package net.iatsuk.jann.bench.annoy.latency;

import net.iatsuk.jann.annoy.ANNIndex;
import net.iatsuk.jann.annoy.AnnoyIndex;
import net.iatsuk.jann.annoy.IndexType;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.ArrayList;
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
public class BenchmarkAnnoyScanBySearchK {

    @Param({"1000"})
    private int N;
    @Param({"1000"})
    private int NEIGHBOURS;
    @Param({"100"})
    private int DIM;

    private AnnoyIndex index;
    private List<float[]> queries = new ArrayList<>();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkAnnoyScanBySearchK.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() throws IOException {
        // index
        String path = String.format("data/groups_angular_t10_d100_r%s.ann", 1_000_000);
        index = new ANNIndex(DIM, path, IndexType.ANGULAR);
        // queries
        Random rnd = new Random(0xDEADBEEF);
        queries = IntStream.range(0, N).boxed().map(i -> makeRandomVector(rnd, DIM)).collect(Collectors.toList());
    }

    private float[] makeRandomVector(Random rnd, int dim) {
        float[] result = new float[dim];
        for (int i = 0; i < dim; i++) {
            result[i] = rnd.nextFloat() * 2 - 1;
        }
        return result;
    }

    @Benchmark
    public void scanAngularBySearchK500(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 500);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularBySearchK1500(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 1500);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularBySearchK3000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 3000);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularBySearchK5000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 5000);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularBySearchK7500(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 7500);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularBySearchK10000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 10000);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularBySearchK12500(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 12500);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularBySearchK15000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = index.getNearest(query, NEIGHBOURS, 15000);
            bh.consume(neighbours);
        });
    }

}

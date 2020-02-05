package net.iatsuk.jann.bench.annoy.latency;

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
import java.util.ArrayList;
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
public class BenchmarkAnnoyScanByNRecords {

    @Param({"1000"})
    private int N;
    @Param({"1000"})
    private int NEIGHBOURS;
    @Param({"100"})
    private int DIM;

    private List<AnnoyIndex> indexes = new ArrayList<>();
    private List<float[]> queries = new ArrayList<>();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkAnnoyScanByNRecords.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() throws IOException {
        for(int recNo: Arrays.asList(10_000, 25_000, 50_000, 100_000, 250_000, 500_000, 1_000_000)) {
            // index
            String path = String.format("data/groups_angular_t10_d100_r%s.ann", recNo);
            AnnoyIndex index = new ANNIndex(DIM, path, IndexType.ANGULAR);
            indexes.add(index);
        }
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
    public void scanAngularByRecNo10_000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexes.get(0).getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByRecNo25_000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexes.get(1).getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByRecNo50_000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexes.get(2).getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByRecNo100_000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexes.get(3).getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByRecNo250_000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexes.get(4).getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByRecNo500_000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexes.get(5).getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanAngularByRecNo1_000_000(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexes.get(6).getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

}

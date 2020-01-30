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
public class BenchmarkAnnoyScanByMetric {

    @Param({"1000"})
    private int N;
    @Param({"100"})
    private int NEIGHBOURS;
    @Param({"100"})
    private int dim;

    private AnnoyIndex indexAngular;
    private AnnoyIndex indexEuclidean;
    private List<float[]> queries;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkAnnoyScanByMetric.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() throws IOException {
        indexAngular = new ANNIndex(dim, "data/groups_angular.ann", IndexType.ANGULAR);
        indexEuclidean = new ANNIndex(dim, "data/groups_euclidean.ann", IndexType.EUCLIDEAN);
        Random rnd = new Random(0xDEADBEEF);
        queries = IntStream.range(0, N).boxed()
                .map(i -> makeRandomVector(rnd, dim))
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
    public void scanAngularByVector(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexAngular.getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

    @Benchmark
    public void scanEuclideanByVector(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexEuclidean.getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

}

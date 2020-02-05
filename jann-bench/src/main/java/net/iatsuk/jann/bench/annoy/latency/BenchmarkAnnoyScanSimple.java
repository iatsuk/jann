package net.iatsuk.jann.bench.annoy.latency;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
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
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"})
@Warmup(iterations = 3)
@Measurement(iterations = 8)
public class BenchmarkAnnoyScanSimple {

    @Param({"1000"})
    private int N;
    @Param({"1000"})
    private int NEIGHBOURS;
    @Param({"100"})
    private int DIM;
    @Param({"4"})
    private int TREES;

    private AnnoyIndex indexAngular;
    private List<float[]> queries;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkAnnoyScanSimple.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() throws IOException {
        indexAngular = new ANNIndex(DIM, String.format("/Volumes/ramka/groups_angular_t%d_d100_r1000000.ann", TREES), IndexType.ANGULAR);
        Random rnd = new Random(0xDEADBEEF);
        queries = IntStream.range(0, N).boxed().map(i -> rndVector(rnd, DIM)).collect(Collectors.toList());
    }

    private float[] rndVector(Random rnd, int dim) {
        return Floats.toArray(Doubles.asList(
                DoubleStream.generate(rnd::nextFloat).limit(dim).map(r -> r * 2 - 1).toArray()
        ));
    }

    @Benchmark
    public void scanAngular(Blackhole bh) {
        queries.forEach(query -> {
            List<Integer> neighbours = indexAngular.getNearest(query, NEIGHBOURS);
            bh.consume(neighbours);
        });
    }

}

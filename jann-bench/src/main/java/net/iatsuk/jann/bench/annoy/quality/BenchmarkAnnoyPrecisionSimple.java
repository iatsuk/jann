package net.iatsuk.jann.bench.annoy.quality;

import net.iatsuk.jann.annoy.ANNIndex;
import net.iatsuk.jann.annoy.AnnoyIndex;
import net.iatsuk.jann.annoy.IndexType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BenchmarkAnnoyPrecisionSimple {
    private final static int NEIGHBOURS = 1_000;
    private final static int DIM = 100;
    private final static int TREES = 15;
    private final static int SEARCH_K = -1;

    private static AnnoyIndex index;
    private static float[] query;
    private static List<Integer> trueNeighbours;

    public static void main(String[] args) throws IOException {
        setup();

        System.out.println(String.format("query length: %d", query.length));
        System.out.println(String.format("trueNeighbours length: %d", trueNeighbours.size()));

        precision(index);

        tearDown();
    }

    public static void setup() throws IOException {
        index = new ANNIndex(DIM, String.format("data/groups_angular_t%d_d100_r1000000.ann", TREES), IndexType.ANGULAR);
        query = new float[]{0.03488601871021374f, -0.03926630035993843f, -0.12472288439610113f, 0.03709069237189265f, 0.02360414334026862f, 0.15041131720415196f, 0.022161175591816826f, -0.10070606177506326f, 0.1585422820738142f, 0.09900950938957027f, -0.18394536523385416f, -0.05587672304369158f, 0.1311631866759069f, 0.02617777866509993f, -0.1246035302536016f, 0.05301275645564434f, 0.012843003233209116f, 0.04713883096600862f, -0.01908558816040728f, -0.09230717714142966f, 0.09678929631032891f, -0.16157537496137195f, -0.1360522389534573f, -0.15576923596468686f, -0.11767285957439005f, 0.20112685328990607f, 0.12821253197144342f, -0.08555307887085202f, 0.06201257155134966f, 0.030391121382198093f, -0.1106693527864156f, -0.0755186242428472f, 0.06927288649744853f, -0.0716546872656359f, -0.08545322019710327f, 0.04179101451172278f, -0.046469606830678974f, -0.210804736151816f, 0.11763543142569768f, -0.11878385747432865f, 0.14001420951215052f, -0.06187869213616836f, 0.11276468701274789f, -0.09166966442185531f, 0.17242163938249583f, -0.12972037782716084f, -0.026074024826823517f, 0.06108581315009942f, 0.08311758108362877f, 0.05514469788422874f, 0.02524195354600155f, -0.05903201786475763f, 0.013160414035665953f, -0.03183804801707335f, -0.04720492751621384f, 0.03416554720247171f, 0.18670721663582382f, -0.10134418257355692f, 0.10536082549827336f, 0.09501329987374112f, -0.18496485265830212f, 0.1107856865570931f, 0.03296014867093482f, 0.040050618898500714f, -0.008531274455551628f, 0.10126168252247719f, -0.1707857650088359f, -0.15341432731784005f, 0.028914664430560167f, -0.09580903068251619f, -0.048042789192243224f, 0.05533975116335034f, 0.07972263207402064f, -0.07133772024451678f, -0.01290341203075754f, -0.020421272979537976f, -0.055333566458689885f, -0.02993459565422041f, -0.06203777293814995f, -0.054112121530285454f, -0.1443548400058729f, 0.005000419218596455f, 0.0664121904361472f, -0.06550000452954184f, -0.11187927271878269f, -0.038350771167912266f, -0.06381700417331325f, -0.07596168140878108f, -0.08943353828948813f, 0.0015854452681285257f, -0.08997306359424422f, 0.04589971878183809f, -0.1542470100255938f, 0.24519748986550452f, 0.13564542853192796f, 0.008949991783134851f, 0.04973944874955985f, -0.14277751775229008f, 0.06850589021296354f, 0.1523716009220226f};
        trueNeighbours = Files.readAllLines(Paths.get(String.format("data/groups_angular_t%d_d100_r1000000.true", TREES))).stream()
                .filter(line -> !line.isEmpty())
                .flatMap(line -> Arrays.stream(line.split(", ")))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static void precision(AnnoyIndex index) {
        Set<Integer> trueNeighbours1000 = trueNeighbours.stream().limit(NEIGHBOURS).collect(Collectors.toSet());
        Set<Integer> trueNeighbours1500 = new HashSet<>(trueNeighbours);
        List<Integer> predicted = index.getNearest(query, NEIGHBOURS, SEARCH_K);
        System.out.println("predicted size: " + predicted.size());

        long tp = predicted.stream().filter(trueNeighbours1000::contains).count();
        long fn = tp - predicted.stream().filter(trueNeighbours1500::contains).count();
        long fp = predicted.stream().filter(i -> !trueNeighbours1500.contains(i)).count();

        double precision = tp * 1d / (tp + fp);
        double recall = tp * 1d / (tp + fn);

        long top15k = predicted.stream().filter(trueNeighbours1500::contains).count();

        System.out.println(String.format("SearchK=%d\ttop1k: %.2f\ttop1.5k: %.2f", SEARCH_K, 1d * tp / NEIGHBOURS, 1d * top15k / NEIGHBOURS));
    }

    public static void tearDown() throws IOException {
        index.close();
    }

}

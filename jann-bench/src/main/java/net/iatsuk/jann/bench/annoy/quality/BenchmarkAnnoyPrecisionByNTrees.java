package net.iatsuk.jann.bench.annoy.quality;

import com.spotify.annoy.ANNIndex;
import com.spotify.annoy.AnnoyIndex;
import com.spotify.annoy.IndexType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BenchmarkAnnoyPrecisionByNTrees {
    private final static int NEIGHBOURS = 1_000;
    private final static int DIM = 100;

    private static AnnoyIndex indexAngular1;
    private static AnnoyIndex indexAngular10;
    private static AnnoyIndex indexAngular100;
    private static AnnoyIndex indexAngular50;
    private static float[] query;
    private static Set<Integer> trueNeighbours;

    public static void main(String[] args) throws IOException {
        setup();

        System.out.println(String.format("query length: %d", query.length));
        System.out.println(String.format("trueNeighbours length: %d", trueNeighbours.size()));

        float[] precision = precision(indexAngular1, indexAngular10, indexAngular100, indexAngular50);
        System.out.println(Arrays.toString(precision));

        tearDown();
    }

    public static void setup() throws IOException {
        indexAngular1 = new ANNIndex(DIM, "data/groups_angular_t5_d100_total.ann", IndexType.ANGULAR);
        indexAngular10 = new ANNIndex(DIM, "data/groups_angular_t15_d100_total.ann", IndexType.ANGULAR);
        indexAngular100 = new ANNIndex(DIM, "data/groups_angular_t25_d100_total.ann", IndexType.ANGULAR);
        indexAngular50 = new ANNIndex(DIM, "data/groups_angular_t50_d100_total.ann", IndexType.ANGULAR);
        query = new float[]{0.5526169214319689f, -0.6173932669896345f, -1.1965971404085334f, 0.8392220855452609f, 0.2102931642689041f, 2.028323598865528f, 0.4665086467040577f, -0.8412081362051627f, 1.7466708578275678f, 1.2508126211453303f, -1.8790263000757927f, -0.6170111925017366f, 1.5405586634870043f, 0.31681735107306364f, -1.2664453177894102f, 0.3423244736338054f, 0.02247254382789629f, 0.8629174937180463f, 0.0030412306655443477f, -0.8772507391688604f, 1.2940709058452482f, -1.8192006729359929f, -1.5402323215086613f, -1.9673846258700276f, -1.1094548082627882f, 2.4555716982447198f, 1.6993769240470789f, -0.964369136261241f, 0.8383296929837394f, 0.173222572747122f, -1.8281978133826815f, -1.0364836454625121f, 0.7665963451073936f, -0.7739259320754005f, -0.8726882956196176f, 0.37381639073064576f, -0.6321783883475879f, -2.2211746624315487f, 1.045341245537085f, -2.0287581745894485f, 1.5236214427520978f, -0.7569186920293118f, 1.359565378775612f, -1.1939717487821009f, 2.034723552980574f, -1.763437323118643f, -0.1370826686149047f, 0.8078646362831332f, 1.0757158259036985f, 0.16619416381536545f, 0.4327156994023141f, -0.7011636711755436f, 0.10728305389479309f, -0.43888302681704666f, -0.12343622409719024f, 0.5544333984986184f, 2.5985039983822014f, -1.295282591655628f, 1.351075603246701f, 1.2983114713361836f, -2.3204150448300656f, 1.028278817490984f, 0.16339272384080872f, 0.3671002401197906f, -0.004475425885426931f, 0.9341687332749609f, -2.0619957931320156f, -1.5395298593562305f, 0.6175813034531834f, -1.1650144939000284f, -0.8562089174287855f, 0.2530640432732222f, 1.2728532716279364f, -1.0937807398473056f, 0.048830329030418784f, -0.23181221361096216f, -0.18922945037292171f, -0.5954630699300836f, -1.061399212369651f, -0.8237984924668706f, -1.9056271319987974f, -0.11857346300754512f, 0.6618229614962001f, -0.7108315645673086f, -1.3867772471278137f, -0.9994618161603808f, -0.6665369017389287f, -0.7416854367336831f, -0.503862077911252f, 0.013621873129193288f, -1.1727005267594415f, 0.22221463169641698f, -1.97623602648862f, 2.9777260184176355f, 1.834206243713148f, -0.20380226250128425f, 0.6352519772128422f, -1.6575829264393298f, 0.5625853683491879f, 1.8836134721750548f};
        trueNeighbours = Files.readAllLines(Paths.get("data/true_neighbours.dat")).stream()
                .filter(line -> !line.isEmpty())
                .flatMap(line -> Arrays.stream(line.split(", ")))
                .map(String::trim)
                .map(Integer::parseInt)
                .limit(NEIGHBOURS)
                .collect(Collectors.toSet());
    }

    public static float[] precision(AnnoyIndex... indexes) {
        float[] curve = new float[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            AnnoyIndex index = indexes[i];
            long coincided = index.getNearest(query, NEIGHBOURS).stream()
                    .filter(trueNeighbours::contains)
                    .count();
            curve[i] = 1f * coincided / NEIGHBOURS;
            System.out.println(String.format("Index %d done. Result: %f", i, curve[i]));
        }
        return curve;
    }

    public static void tearDown() throws IOException {
        indexAngular1.close();
        indexAngular10.close();
        indexAngular100.close();
    }

}

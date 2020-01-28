package net.iatsuk.jaan.visual;

import com.spotify.annoy.jni.base.Annoy;
import com.spotify.annoy.jni.base.AnnoyIndex;
import net.iatsuk.jaan.visual.datasets.*;
import net.iatsuk.jaan.visual.pojo.Point2D;
import net.iatsuk.jaan.visual.utils.DrawImage;

import java.util.Arrays;
import java.util.List;

public class DrawDatasets {

    public static void main(String[] args) {
        {
            Dataset2D dataset = new EmptyDataset();
            DrawImage.draw("empty.jpg", 1024, 1024, false, dataset.points());
        }

        {
            Dataset2D dataset = new UniformDataset(10000, 1024, 1024, 0xDEADBEEF);
            DrawImage.draw("uniform.jpg", 1024, 1024, false, dataset.points());
        }

        {
            Dataset2D dataset = new NormalDataset(10000, 1024, 1024, 0xDEADBEEF);
            DrawImage.draw("normal.jpg", 1024, 1024, false, dataset.points());
        }

        {
            Dataset2D dataset = new CustomDataset1(10000, 1024, 1024, 0xDEADBEEF);
            Point2D[] points = dataset.points();

            Annoy.Builder builder = Annoy.newIndex(2, Annoy.Metric.EUCLIDEAN);
            builder.setSeed(0xDEADBEEF);
            for (int i = 0; i < points.length; i++) {
                Point2D point = points[i];
                float x = 1f * point.x;
                float y = 1f * point.y;
                System.out.println(i + "\t" + x + "\t" + y);
                builder.addItem(i, Arrays.asList(x, y));
            }
            AnnoyIndex index = builder.build(100);

            points = new Point2D[points.length + 1];
            System.arraycopy(dataset.points(), 0, points, 0, points.length - 1);
            Point2D targetPoint = Point2D.of(256, 768, 0xFF0000);
            points[points.length - 1] = targetPoint;
            List<Integer> marked = index.getNearestByVector(Arrays.asList(1f*targetPoint.x, 1f*targetPoint.y), 2000);
            for (int idx: marked) {
                points[idx].color = 0x00FFFF;
            }
            index.close();

            DrawImage.draw("custom1.jpg", 1024, 1024, true, points);
        }
    }

}

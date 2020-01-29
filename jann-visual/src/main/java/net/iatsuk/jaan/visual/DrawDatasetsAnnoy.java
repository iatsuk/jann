package net.iatsuk.jaan.visual;

import com.spotify.annoy.jni.base.Annoy;
import com.spotify.annoy.jni.base.AnnoyIndex;
import net.iatsuk.jaan.test.datasets.*;
import net.iatsuk.jaan.test.pojo.Point2D;
import net.iatsuk.jaan.visual.utils.DrawImage;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DrawDatasetsAnnoy {

    public static void main(String[] args) {
        {
            Dataset2D dataset = new EmptyDataset();
            DrawImage.draw("emptyAnnoy.jpg", 1024, 1024, true, findNeighbours(dataset.points()));
        }

        {
            Dataset2D dataset = new UniformDataset(10000, 1024, 1024, 0xDEADBEEF);
            DrawImage.draw("uniformAnnoy.jpg", 1024, 1024, true, findNeighbours(dataset.points()));
        }

        {
            Dataset2D dataset = new NormalDataset(10000, 1024, 1024, 0xDEADBEEF);
            DrawImage.draw("normalAnnoy.jpg", 1024, 1024, true, findNeighbours(dataset.points()));
        }

        {
            Dataset2D dataset = new CustomDataset1(10000, 1024, 1024, 0xDEADBEEF);
            DrawImage.draw("custom1Annoy.jpg", 1024, 1024, true, findNeighbours(dataset.points()));
        }
    }

    private static Point2D[] findNeighbours(Point2D[] points) {
        // generate ann index
        Annoy.Builder builder = Annoy.newIndex(2, Annoy.Metric.ANGULAR);
        builder.setSeed(0xDEADBEEF);
        for (int i = 0; i < points.length; i++) {
            // move the origin into center of image
            Point2D point = points[i];
            float x = 1f * point.x - 512;
            float y = 1f * point.y - 512;
            builder.addItem(i, Arrays.asList(x, y));
        }
        AnnoyIndex index = builder.build(100);

        // add a target point to points
        Point2D targetPoint = Point2D.of(-510 + 512, -9 + 512, 0xFF0000);
        points = Arrays.copyOf(points, points.length + 1);
        points[points.length - 1] = targetPoint;

        // find a nearest neighbours to the target point
        List<Integer> marked = index.getNearestByVector(Arrays.asList(1f*targetPoint.x - 512, 1f*targetPoint.y - 512), 1000);
        int color = 0x0FFFFF;
        for (int i = 0; i < marked.size(); i++) {
            // fill the nearest neighbours of the target point
            Point2D point = points[marked.get(i)];
            point.color = color;
            if (i % 200 == 0) {
                color = new Color(color).darker().getRGB();
            }
        }

        // close index and return result
        index.close();
        return points;
    }

}

package net.iatsuk.jaan.test.datasets;

import net.iatsuk.jaan.test.pojo.Point2D;

import java.util.Random;

public class NormalDataset implements Dataset2D {
    private final Point2D[] points;

    public NormalDataset(int n, int maxX, int maxY, long seed) {
        Random random = new Random(seed);
        points = new Point2D[n];
        for (int i = 0; i < n; i++) {
            int x = (int) (Math.abs(random.nextGaussian()) * maxX);
            int y = (int) (Math.abs(random.nextGaussian()) * maxY);
            points[i] = Point2D.of(x, y);
        }
    }

    @Override
    public Point2D[] points() {
        return points;
    }
}

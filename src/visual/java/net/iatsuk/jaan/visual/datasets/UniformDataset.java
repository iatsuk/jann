package net.iatsuk.jaan.visual.datasets;

import net.iatsuk.jaan.visual.pojo.Point2D;

import java.util.Random;

public class UniformDataset implements Dataset2D {
    private final Point2D[] points;

    public UniformDataset(int n, int maxX, int maxY, long seed) {
        Random random = new Random(seed);
        points = new Point2D[n];
        for (int i = 0; i < n; i++) {
            int x = random.nextInt(maxX);
            int y = random.nextInt(maxY);
            points[i] = Point2D.of(x, y);
        }
    }

    @Override
    public Point2D[] points() {
        return points;
    }
}

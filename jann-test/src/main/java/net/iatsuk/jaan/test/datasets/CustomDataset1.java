package net.iatsuk.jaan.test.datasets;

import net.iatsuk.jaan.test.pojo.Point2D;

import java.util.Random;

public class CustomDataset1 implements Dataset2D {
    private final Point2D[] points;

    public CustomDataset1(int n, int maxX, int maxY, long seed) {
        Random random = new Random(seed);
        points = new Point2D[n];
        for (int i = 0; i < n; i++) {
            int range1 = n / 6;
            int range2 = n / 3;
            int range3 = n * 2 / 5;

            if (i < range1) {
                // normal1
                int x = (int) (random.nextGaussian() * maxX * 0.07 + maxX / 4);
                int y = (int) (random.nextGaussian() * maxY * 0.07 + maxY / 4);
                points[i] = Point2D.of(x, y);
            } else if (i < range1 + range2) {
                // normal2
                int x = (int) (random.nextGaussian() * maxX * 0.15 + maxX * 2 / 3);
                int y = (int) (random.nextGaussian() * maxY * 0.15 + maxY / 3);
                points[i] = Point2D.of(x, y);
            } else if (i < range1 + range2 + range3) {
                // normal3
                int x = (int) (random.nextGaussian() * maxX * 0.30 + maxX * 3 / 4);
                int y = (int) (random.nextGaussian() * maxY * 0.30 + maxY * 3 / 4);
                points[i] = Point2D.of(x, y);
            } else {
                // uniform
                int x = random.nextInt(maxX);
                int y = random.nextInt(maxY);
                points[i] = Point2D.of(x, y);
            }
        }
    }

    @Override
    public Point2D[] points() {
        return points;
    }
}

package net.iatsuk.jaan.test.datasets;

import net.iatsuk.jaan.test.pojo.Point2D;

public class EmptyDataset implements Dataset2D {

    @Override
    public Point2D[] points() {
        return new Point2D[0];
    }

}

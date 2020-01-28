package net.iatsuk.jaan.visual.datasets;

import net.iatsuk.jaan.visual.pojo.Point2D;

public class EmptyDataset implements Dataset2D {

    @Override
    public Point2D[] points() {
        return new Point2D[0];
    }

}

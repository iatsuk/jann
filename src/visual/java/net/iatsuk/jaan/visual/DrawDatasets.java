package net.iatsuk.jaan.visual;

import net.iatsuk.jaan.visual.datasets.*;
import net.iatsuk.jaan.visual.utils.DrawImage;

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
            DrawImage.draw("custom1.jpg", 1024, 1024, true, dataset.points());
        }
    }

}

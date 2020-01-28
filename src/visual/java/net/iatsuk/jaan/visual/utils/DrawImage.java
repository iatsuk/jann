package net.iatsuk.jaan.visual.utils;

import net.iatsuk.jaan.visual.pojo.Point2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawImage {

    public static void draw(String name, int width, int height, boolean colored, Point2D... points) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setColor(new Color(0xFFFFFF));
        g2d.fillRect(0, 0, width, height);

        int size = 4;
        for (Point2D point : points) {
            Color color = colored ? new Color(point.color) : Color.BLACK;
            g2d.setColor(color);
            g2d.fillRect(point.x - size/2, point.y - size/2, size, size);
        }

        try {
            File dir = new File("visuals");
            dir.mkdirs();
            ImageIO.write(image, "jpg", new File(dir, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DrawImage.draw("TestImage.jpg", 128, 128, true,
                Point2D.of(64, 64),
                Point2D.of(32, 32, 0xFFFF00),
                Point2D.of(96, 96, 0xFF00FF)
        );
    }
}

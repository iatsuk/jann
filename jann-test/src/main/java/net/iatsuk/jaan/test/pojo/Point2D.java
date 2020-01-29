package net.iatsuk.jaan.test.pojo;

public class Point2D {
    public int x;
    public int y;
    public int color;

    public static Point2D of(int x, int y) {
        return new Point2D(x, y);
    }

    public static Point2D of(int x, int y, int color) {
        return new Point2D(x, y, color);
    }

    public Point2D(int x, int y) {
        this(x, y, 0x000000);
    }

    public Point2D(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}

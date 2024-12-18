import java.util.Objects;

public class Point {
    public static Point UP    = new Point(-1,0);
    public static Point DOWN  = new Point(1,0);
    public static Point LEFT  = new Point(0,-1);
    public static Point RIGHT = new Point(0,1);

    public final int r, c;

    public Point(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public Point add(Point p) {
        return new Point(p.r + this.r, p.c + this.c);
    }

    public int dist(Point p) {
        return Math.abs(this.r - p.r) + Math.abs(this.c - p.c);
    }

    public Point ccw() {
        return new Point(-this.c, this.r);
    }

    public Point cw() {
        return new Point(this.c, -this.r);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            return ((Point) obj).r == this.r && ((Point) obj).c == this.c;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.c, this.r);
    }
}
import java.util.*;
import java.util.stream.*;

public class Grid18 {
    public int[][] walls;
    public int[][] dist;
    public Point start = new Point(0,0);
    public Point end = null;
    public int rows;
    public int columns;

    public Grid18(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.walls = new int[rows][columns];
        this.dist = new int[rows][columns];
        this.end = new Point(rows - 1, columns - 1);
        this.resetDistances();
    }

    public void resetDistances() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                this.dist[r][c] = this.rows * this.columns;
            }
        }
        this.dist[0][0] = 0;
    }

    public void addWall(int r, int c) {
        this.walls[r][c] = 1;
    }

    public void addWall(Point p) {
        this.addWall(p.r, p.c);
    }

    public boolean isWall(int r, int c) {
        if (r < 0 || r >= this.rows || c < 0 || c >= this.columns) {
            return true;
        }
        return this.walls[r][c] == 1;
    }

    public boolean isWall(Point p) {
        return this.isWall(p.r, p.c);
    }

    public int getDist(int r, int c) {
        if (r < 0 || r >= this.rows || c < 0 || c >= this.columns) {
            return this.rows * this.columns;
        }
        return this.dist[r][c];
    }

    public int getDist(Point p) {
        return this.getDist(p.r, p.c);
    }

    public void setDist(int r, int c, int dist) {
        this.dist[r][c] = dist;
    }

    public void setDist(Point p, int dist) {
        this.setDist(p.r, p.c, dist);
    }

    public void print() {
        for (int r = -1; r <= this.rows; r++) {
            for (int c = -1; c <= this.columns; c++) {
                if (this.isWall(r,c)) {
                    System.out.print("🧱");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
}
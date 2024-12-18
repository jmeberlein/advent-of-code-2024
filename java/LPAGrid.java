import java.util.*;
import java.util.stream.*;

public class LPAGrid {
    private int[][] walls;
    private int[][] dist;
    private int[][] heur;
    private int[][] rhs;
    private final int INFINITY;
    private LPAQueue queue = new LPAQueue();
    private final Point[] DIRECTIONS = {Point.UP, Point.DOWN, Point.LEFT, Point.RIGHT};

    public final Point start = new Point(0,0);
    public final Point end;
    public final int rows;
    public final int columns;

    public int compare(Point a, Point b) {
        int[] scoreA = this.getScore(a);
        int[] scoreB = this.getScore(b);
        return (scoreA[0] == scoreB[0]) ? (scoreA[0] - scoreB[0])
                                        : (scoreA[1] - scoreB[1]);
    }

    private class LPAQueue extends PriorityQueue<Point> {
        public LPAQueue() {
            super((a,b) -> LPAGrid.this.compare(a,b));
        }

        public boolean hasNext() {
            if (this.isEmpty()) {
                return false;

            }

            return !LPAGrid.this.isConsistent(LPAGrid.this.end) ||
                   LPAGrid.this.compare(LPAGrid.this.end, this.peek()) >= 0;
        }
    }

    public LPAGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.walls = new int[rows][columns];
        this.dist = new int[rows][columns];
        this.heur = new int[rows][columns];
        this.rhs = new int[rows][columns];
        this.end = new Point(rows - 1, columns - 1);
        this.INFINITY = 2 * rows * columns;

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                this.dist[r][c] = this.rhs[r][c] = this.INFINITY;
                this.heur[r][c] = Math.abs(this.end.r - r) + Math.abs(this.end.c - c);
            }
        }
        this.rhs[0][0] = 0;
        this.queue.add(this.start);
        this.update();
    }

    private void update() {
        while (this.queue.hasNext()) {
            Point curr = this.queue.poll();

            if (this.getDist(curr) > this.getRHS(curr)) {
                this.setDist(curr, this.getRHS(curr));
            } else if (this.getDist(curr) < this.getRHS(curr)) {
                this.setDist(curr, this.INFINITY);
                if (!this.isConsistent(curr)) {
                    this.queue.remove(curr);
                    this.queue.add(curr);
                }
            }
        }
    }

    public void inspect() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                if (this.isWall(new Point(r,c))) {
                    System.out.print("   ");
                } else {
                    System.out.printf("%2d ", this.dist[r][c]);
                }
            }
            System.out.println();
        }
    }

    private boolean isInBounds(Point p) {
        return p.r >= 0 && p.r < this.rows && p.c >= 0 && p.c < this.columns;
    }

    private boolean isWall(Point p) {
        return !this.isInBounds(p) || this.walls[p.r][p.c] == 1;
    }

    public int getDist(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return this.INFINITY;
        }

        return this.dist[p.r][p.c];
    }

    private int getRHS(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return this.INFINITY;
        }

        return this.rhs[p.r][p.c];
    }

    private int getHeuristic(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return this.INFINITY;
        }

        return this.heur[p.r][p.c];
    }

    private int[] getScore(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return new int[]{this.INFINITY, this.INFINITY};
        }

        int distOrEst = Math.min(this.dist[p.r][p.c], this.rhs[p.r][p.c]);
        return new int[]{Math.min(this.INFINITY, distOrEst + this.heur[p.r][p.c]), distOrEst};
    }

    private boolean isConsistent(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return true;
        }

        return this.dist[p.r][p.c] == this.rhs[p.r][p.c];
    }

    private void setDist(Point p, int dist) {
        if (!this.isInBounds(p) || this.isWall(p) || this.getDist(p) == dist) {
            return;
        }

        this.dist[p.r][p.c] = dist;
        for (Point dir : this.DIRECTIONS) {
            this.updateRHS(p.add(dir));
        }
        this.queue.remove(p);
        this.queue.add(p);
    }

    private void updateRHS(Point p) {
        if (!this.isInBounds(p) || this.isWall(p) || (p.r == 0 && p.c == 0)) {
            return;
        }

        int minDist = this.INFINITY;
        for (Point dir : this.DIRECTIONS) {
            int test = this.getDist(p.add(dir)) + 1;
            if (test < minDist && !isWall(p.add(dir))) {
                minDist = test;
            }
        }

        // if (this.rhs[p.r][p.c] > minDist) {
            this.rhs[p.r][p.c] = minDist;
            // if (this.dist[p.r][p.c] != minDist) {
                this.queue.remove(p);
                this.queue.add(p);
            // }
        // }
    }

    public boolean addWall(Point p) {
        if (this.isInBounds(p) && !this.isWall(p)) {
            this.walls[p.r][p.c] = 1;
            this.heur[p.r][p.c] = this.INFINITY;
            this.rhs[p.r][p.c] = this.INFINITY;
            this.dist[p.r][p.c] = this.INFINITY;
            for (Point dir : this.DIRECTIONS) {
                this.updateRHS(p.add(dir));
            }
            this.queue.remove(p);
            this.update();
        }

        return this.getDist(this.end) < this.INFINITY;
    }
}
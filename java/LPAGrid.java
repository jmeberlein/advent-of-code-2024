import java.util.*;

public class LPAGrid {
    private int[][] walls;
    private int[][] dist;
    private int[][] exp;
    private final int INFINITY;
    private LPAQueue queue = new LPAQueue();
    private final Point[] DIRECTIONS = {Point.UP, Point.DOWN, Point.LEFT, Point.RIGHT};

    public Point start = new Point(0,0);
    public Point end = null;
    public final int rows;
    public final int columns;

    private class LPAQueue extends PriorityQueue<Point> {
        public LPAQueue() {
            super((a,b) -> LPAGrid.this.getScore(a) - LPAGrid.this.getScore(b));
        }

        public boolean hasNext() {
            if (this.isEmpty()) {
                return false;
            }

            return !LPAGrid.this.isConsistent(LPAGrid.this.end) ||
                   LPAGrid.this.getScore(LPAGrid.this.end) >= LPAGrid.this.getScore(this.peek());
        }
    }

    public LPAGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.walls = new int[rows][columns];
        this.dist = new int[rows][columns];
        this.exp = new int[rows][columns];
        this.end = new Point(rows - 1, columns - 1);
        this.INFINITY = 2 * rows * columns;

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                this.dist[r][c] = this.exp[r][c] = r+c;
            }
        }
    }

    public LPAGrid(int rows, int columns, Point start) {
        this.rows = rows;
        this.columns = columns;
        this.walls = new int[rows][columns];
        this.dist = new int[rows][columns];
        this.exp = new int[rows][columns];
        this.end = new Point(rows - 1, columns - 1);
        this.start = start;
        this.INFINITY = 2 * rows * columns;

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                this.dist[r][c] = this.exp[r][c] = this.INFINITY;
            }
        }
        this.exp[start.r][start.c] = 0;
        this.queue.add(start);
        this.update();
    }

    public LPAGrid(List<String> file) {
        this.rows = file.size();
        this.columns = file.get(0).length();
        this.walls = new int[rows][columns];
        this.dist = new int[rows][columns];
        this.exp = new int[rows][columns];
        this.INFINITY = 2 * rows * columns;

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                switch (file.get(r).charAt(c)) {
                case 'S':
                    this.walls[r][c] = 0;
                    this.exp[r][c] = 0;
                    this.dist[r][c] = this.INFINITY;
                    this.start = new Point(r, c);
                    break;
                case 'E':
                    this.end = new Point(r, c);
                    // Fall through, because apart from setting this.end, it's otherwise a normal free space
                case '.':
                    this.walls[r][c] = 0;
                    this.exp[r][c] = this.INFINITY;
                    this.dist[r][c] = this.INFINITY;
                    break;
                case '#':
                    this.walls[r][c] = 1;
                    break;
                }
            }
        }

        this.queue.add(start);
        this.update();
    }

    private void update() {
        while (this.queue.hasNext()) {
            Point curr = this.queue.poll();

            if (this.getDist(curr) > this.getExp(curr)) {
                this.setDist(curr, this.getExp(curr));
            } else if (this.getDist(curr) < this.getExp(curr)) {
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

    public boolean isWall(Point p) {
        return !this.isInBounds(p) || this.walls[p.r][p.c] == 1;
    }

    public int getDist(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return this.INFINITY;
        }

        return this.dist[p.r][p.c];
    }

    private int getExp(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return this.INFINITY;
        }

        return this.exp[p.r][p.c];
    }

    private int getScore(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return this.INFINITY;
        }

        return Math.min(this.dist[p.r][p.c], this.exp[p.r][p.c]);
    }

    private boolean isConsistent(Point p) {
        if (!this.isInBounds(p) || this.isWall(p)) {
            return true;
        }

        return this.dist[p.r][p.c] == this.exp[p.r][p.c];
    }

    private void setDist(Point p, int dist) {
        if (!this.isInBounds(p) || this.isWall(p) || this.getDist(p) == dist) {
            return;
        }

        this.dist[p.r][p.c] = dist;
        for (Point dir : this.DIRECTIONS) {
            this.updateExp(p.add(dir));
        }
        this.queue.remove(p);
        this.queue.add(p);
    }

    private void updateExp(Point p) {
        if (!this.isInBounds(p) || this.isWall(p) || p.equals(this.start)) {
            return;
        }

        int minDist = this.INFINITY;
        for (Point dir : this.DIRECTIONS) {
            int test = this.getDist(p.add(dir)) + 1;
            if (test < minDist && !isWall(p.add(dir))) {
                minDist = test;
            }
        }

        if (this.exp[p.r][p.c] != minDist) {
            this.exp[p.r][p.c] = minDist;
            this.queue.remove(p);
            this.queue.add(p);
        }
    }

    public boolean addWall(Point p) {
        if (this.isInBounds(p) && !this.isWall(p)) {
            this.walls[p.r][p.c] = 1;
            for (Point dir : this.DIRECTIONS) {
                this.updateExp(p.add(dir));
            }
            this.queue.remove(p);
            this.update();
        }

        return this.getDist(this.end) < this.INFINITY;
    }
}
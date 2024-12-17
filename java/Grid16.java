import java.util.*;
import java.util.stream.*;

public class Grid16 {
    public List<List<Integer>> walls;
    public Set<Point> nodes;
    public Point start = null;
    public Point end = null;

    public Grid16(List<String> file) {
        this.walls = new ArrayList<>();
        for (int r = 0; r < file.size(); r++) {
            this.walls.add(new ArrayList<Integer>());
            for (int c = 0; c < file.get(0).length(); c++) {
                switch (file.get(r).charAt(c)) {
                case 'S':
                    start = new Point(r, c);
                    this.walls.get(r).add(0);
                    break;
                case 'E':
                    end = new Point(r, c);
                    this.walls.get(r).add(0);
                    break;
                case '.':
                    this.walls.get(r).add(0);
                    break;
                case '#':
                    this.walls.get(r).add(1);
                    break;
                }
            }
        }

        nodes = new HashSet<>();
        nodes.add(start);
        nodes.add(end);
        for (int r = 1; r < this.rows()-1; r++) {
            for (int c = 1; c < this.columns()-1; c++) {
                int up = this.get(r-1, c);
                int down = this.get(r+1, c);
                int left = this.get(r, c-1);
                int right = this.get(r, c+1);

                if (this.get(r,c) == 0 && up + down + left + right < 2) {
                    nodes.add(new Point(r,c));
                }
            }
        }
    }

    public int get(int r, int c) {
        return this.walls.get(r).get(c);
    }

    public int get(Point p) {
        return this.walls.get(p.r).get(p.c);
    }

    public int rows() {
        return this.walls.size();
    }

    public int columns() {
        return this.walls.get(0).size();
    }

    public Deer spawnDeer() {
        return new Deer(this.start);
    }

    public void print() {
        print(null);
    }

    public void print(Deer deer) {
        for (int r = 0; r < this.rows(); r++) {
            for (int c = 0; c < this.columns(); c++) {
                if (deer != null && deer.location.equals(new Point(r,c))) {
                    System.out.print("🦌");
                } else if (r == end.r && c == end.c) {
                    System.out.print("🏁");
                } else if (this.nodes.contains(new Point(r,c))) {
                    System.out.print("❗");
                } else if (this.get(r,c) == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print("🧱");
                }
            }
            System.out.println();
        }
    }

    public class Deer {
        public Point location;
        public Point direction;
        public int score;
        public List<Point> route;

        private Deer(Point location) {
            this(location, Point.RIGHT, 0, new ArrayList<Point>());
        }

        private Deer(Point location, Point direction, int score, List<Point> route) {
            this.location = location;
            this.direction = direction;
            this.score = score;
            this.route = route;
        }

        public Deer clone() {
            return new Deer(this.location, this.direction, this.score, new ArrayList<>(this.route));
        }

        public Deer cw() {
            return new Deer(this.location, this.direction.cw(), this.score + 1000, new ArrayList<>(this.route));
        }

        public Deer ccw() {
            return new Deer(this.location, this.direction.ccw(), this.score + 1000, new ArrayList<>(this.route));
        }

        public void setDirection(Point direction) {
            if (!this.direction.equals(direction)) {
                score += 1000;
            }

            this.direction = direction;
        }

        public void prance() {
            if (Grid16.this.get(this.location.add(this.direction)) == 1) {
                if (Grid16.this.get(this.location.add(this.direction.cw())) == 1 &&
                        Grid16.this.get(this.location.add(this.direction.ccw())) == 1) {
                    this.direction = this.direction.cw().cw();
                    this.score += 2000;
                } else if (Grid16.this.get(this.location.add(this.direction.cw())) == 1) {
                    this.direction = this.direction.ccw();
                    this.score += 1000;
                } else {
                    this.direction = this.direction.cw();
                    this.score += 1000;
                }
            }
            this.route.add(this.direction);

            do {
                if (Grid16.this.get(this.location.add(this.direction)) == 1) {
                    if (Grid16.this.get(this.location.add(this.direction.cw())) == 1 &&
                            Grid16.this.get(this.location.add(this.direction.ccw())) == 1) {
                        this.direction = this.direction.cw().cw();
                        this.score += 2000;
                    } else if (Grid16.this.get(this.location.add(this.direction.cw())) == 1) {
                        this.direction = this.direction.ccw();
                        this.score += 1000;
                    } else {
                        this.direction = this.direction.cw();
                        this.score += 1000;
                    }
                }

                this.location = this.location.add(this.direction);
                this.score += 1;
            } while (!Grid16.this.nodes.contains(this.location));
        }

        public Set<Point> path() {
            Set<Point> path = new HashSet<>();
            Point curr = Grid16.this.start;
            path.add(curr);
            for (Point heading : this.route) {
                Point direction = heading;
                do {
                    if (Grid16.this.get(curr.add(direction)) == 1) {
                        if (Grid16.this.get(curr.add(direction.cw())) == 1) {
                            direction = direction.ccw();
                        } else {
                            direction = direction.cw();
                        }
                    }

                    curr = curr.add(direction);
                    path.add(curr);
                } while (!Grid16.this.nodes.contains(curr));
            }
            return path;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Deer) {
                Deer d = (Deer) obj;
                return d.location.equals(this.location) && d.direction.equals(this.direction) && d.score == this.score;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.location.r + 20 * this.location.c + 400 * this.direction.r + 4000 * this.direction.c + 40000 * this.score + 40000000 * this.route.hashCode();
        }
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code8A {
    public static class Point {
        public final int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public Point mult(int a) {
            return new Point(a*this.r, a*this.c);
        }

        public Point add(Point p) {
            return new Point(p.r + this.r, p.c + this.c);
        }

        public Point sub(Point p) {
            return new Point(this.r - p.r, this.c - p.c);
        }

        public boolean equals(Object o) {
            if (o instanceof Point) {
                return this.r == ((Point) o).r && this.c == ((Point) o).c;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return 1000*this.r + this.c;
        }
    }

    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        Map<Character, List<Point>> antennas = new HashMap<>();
        int rows = file.size(), columns = file.get(0).length();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (file.get(r).charAt(c) != '.') {
                    if (!antennas.containsKey(file.get(r).charAt(c))) {
                        antennas.put(file.get(r).charAt(c), new ArrayList<>());
                    }
                    antennas.get(file.get(r).charAt(c)).add(new Point(r, c));
                }
            }
        }

        Set<Point> antinodes = new HashSet<>();
        for (List<Point> list : antennas.values()) {
            for (Point p1 : list) {
                for (Point p2 : list) {
                    if (!p1.equals(p2)) {
                        Point p3 = p1.mult(2).sub(p2);
                        if (p3.r >= 0 && p3.r < rows && p3.c >= 0 && p3.c < columns) {
                            antinodes.add(p3);
                        }
                    }
                }
            }
        }

        System.out.println(antinodes.size());
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code6B {
    public static class Point {
        public final int r, c, dir;
        
        public Point(int r, int c) {
            this.r = r;
            this.c = c;
            this.dir = 3;
        }
        
        public Point(int r, int c, int dir) {
            this.r = r;
            this.c = c;
            this.dir = dir;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                return this.r == ((Point) obj).r && 
                        this.c == ((Point) obj).c && 
                        this.dir == ((Point) obj).dir;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return dir * 1000000 + r * 1000 + c;
        }

        public Point step() {
            switch(dir) {
            case 0:
                return new Point(r, c+1, 0);
            case 1:
                return new Point(r+1, c, 1);
            case 2:
                return new Point(r, c-1, 2);
            case 3:
                return new Point(r-1, c, 3);
            default:
                return null;
            }
        }

        public Point turn() {
            return new Point(r, c, (dir+1)%4);
        }
    }

    public static boolean hasLoop(List<String> map, Point start, Point obstacle) {
        int maxR = map.size();
        int maxC = map.get(0).length();
        Set<Point> path = new HashSet<>();
        Point curr = start;
        Point next;
        while (curr.r >= 0 && curr.r < maxR && curr.c >= 0 && curr.c < maxC) {
            if (path.contains(curr)) {
                return true;
            } else {
                path.add(curr);
            }
            next = curr.step();
            if (next.r < 0 || next.r >= maxR || next.c < 0 || next.c >= maxC) {
                return false;
            } else if (map.get(next.r).charAt(next.c) == '#' || (next.r == obstacle.r && next.c == obstacle.c)) {
                next = curr.turn();
            }
            curr = next;
        }
        return false;
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

        int maxR = file.size();
        int maxC = file.get(0).length();

        Point start = null;
        for (int i = 0; i < maxR; i++) {
            for (int j = 0; j < maxC; j++) {
                if (file.get(i).charAt(j) == '^') {
                    start = new Point(i, j);
                }
            }
        }

        Point next, curr = start;
        Set<List<Integer>> obstacles = new HashSet<>();
        while (curr.r >= 0 && curr.r < maxR && curr.c >= 0 && curr.c < maxC) {
            next = curr.step();
            if (next.r < 0 || next.r >= maxR || next.c < 0 || next.c >= maxC) {
                break;
            } else if (file.get(next.r).charAt(next.c) == '#') {
                next = curr.turn();
            } else if (hasLoop(file, start, next)) {
                obstacles.add(Arrays.asList(next.r, next.c));
            }
            curr = next;
        }

        System.out.println(obstacles.size());
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class Code6A {
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
            if (obj instanceof Code6A.Point) {
                return this.r == ((Code6A.Point) obj).r && 
                        this.c == ((Code6A.Point) obj).c && 
                        this.dir == ((Code6A.Point) obj).dir;
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

        Point curr = null;
        Point next;
        List<List<Integer>> obstacles = new ArrayList<>();
        Set<List<Integer>> path = new HashSet<>();
        for (int i = 0; i < maxR; i++) {
            for (int j = 0; j < maxC; j++) {
                switch(file.get(i).charAt(j)) {
                case '#':
                    obstacles.add(Arrays.asList(i,j));
                    break;
                case '^':
                    curr = new Point(i, j);
                    break;
                }
            }
        }

        while (curr.r >= 0 && curr.r < maxR && curr.c >= 0 && curr.c < maxC) {
            path.add(Arrays.asList(curr.r, curr.c));
            next = curr.step();
            if (next.r < 0 || next.r >= maxR || next.c < 0 || next.c >= maxC) {
                break;
            }
            if (obstacles.contains(Arrays.asList(next.r, next.c))) {
                next = curr.turn();
            }
            curr = next;
        }

        System.out.println(path.size());
    }
}
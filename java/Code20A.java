import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code20A {
    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        LPAGrid grid = new LPAGrid(file);
        // grid.inspect();

        int count = 0;
        int LIMIT = 100;
        int RADIUS = 2;
        List<Point> offsets = new ArrayList<>();
        for (int r = -RADIUS; r <= RADIUS; r++) {
            for (int c = Math.abs(r) - RADIUS; c <= RADIUS - Math.abs(r); c++) {
                if (r != 0 || c != 0) {
                    offsets.add(new Point(r, c));
                }
            }
        }

        // grid.inspect();

        for (int r = 1; r < grid.rows - 1; r++) {
            for (int c = 1; c < grid.columns - 1; c++) {
                Point p1 = new Point(r, c);
                if (!grid.isWall(p1)) {
                    for (Point offset : offsets) {
                        Point p2 = p1.add(offset);
                        int savings = grid.getDist(p2) - grid.getDist(p1) - Math.abs(offset.r) - Math.abs(offset.c);
                        if (!grid.isWall(p2) && savings >= LIMIT) {
                            // System.out.printf("Found cheat! (%d, %d) -> (%d, %d), with length %d\n", p1.r, p1.c, p2.r, p2.c, savings);
                            count++;
                        }
                    }
                }
            }
        }

        System.out.println(count);
    }
}
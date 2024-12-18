import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code18LPA {
    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        String[] dimensions = file.remove(0).split(",");
        LPAGrid grid = new LPAGrid(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
        int i = 0;
        Point p = null;

        while (true) {
            String[] wall = file.get(i).split(",");
            p = new Point(Integer.parseInt(wall[0]), Integer.parseInt(wall[1]));
            if (!grid.addWall(p)) {
                break;
            }
            i++;
        }
        // grid.inspect();

        System.out.printf("%d,%d\n", p.r, p.c);
    }
}
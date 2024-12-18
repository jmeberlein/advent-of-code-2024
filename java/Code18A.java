import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code18A {
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
        Grid18 grid = new Grid18(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
        for (int i = 0; i < 1024; i++) {
            String[] wall = file.get(i).split(",");
            grid.addWall(Integer.parseInt(wall[0]), Integer.parseInt(wall[1]));
        }
        // grid.print();

        Queue<Point> queue = new PriorityQueue<>((a,b) -> {
            int fa = grid.getDist(a) + a.dist(grid.end);
            int fb = grid.getDist(b) + b.dist(grid.end);
            if (fa == fb) {
                return a.dist(grid.end) - b.dist(grid.end);
            } else {
                return fa - fb;
            }
        });
        queue.add(grid.start);

        Point curr;
        while ((curr = queue.poll()) != null) {
            if (curr.equals(grid.end)) {
                break;
            }

            Point up = curr.add(Point.UP);
            Point down = curr.add(Point.DOWN);
            Point left = curr.add(Point.LEFT);
            Point right = curr.add(Point.RIGHT);

            if (!grid.isWall(up) && grid.getDist(curr) + 1 < grid.getDist(up)) {
                grid.setDist(up, grid.getDist(curr) + 1);
                queue.remove(up);
                queue.add(up);
            }
            if (!grid.isWall(down) && grid.getDist(curr) + 1 < grid.getDist(down)) {
                grid.setDist(down, grid.getDist(curr) + 1);
                queue.remove(down);
                queue.add(down);
            }
            if (!grid.isWall(left) && grid.getDist(curr) + 1 < grid.getDist(left)) {
                grid.setDist(left, grid.getDist(curr) + 1);
                queue.remove(left);
                queue.add(left);
            }
            if (!grid.isWall(right) && grid.getDist(curr) + 1 < grid.getDist(right)) {
                grid.setDist(right, grid.getDist(curr) + 1);
                queue.remove(right);
                queue.add(right);
            }
        }

        System.out.println(grid.getDist(grid.end));
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code16 {
    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        Grid16 grid = new Grid16(file);
        int part1 = part1(grid);
        System.out.println(part1);
        int part2 = part2(grid);
        System.out.println(part2);
    }

    public static int part1(Grid16 grid) {
        Map<Point,Grid16.Deer> dist = new HashMap<>();
        Queue<Point> queue = new PriorityQueue<>((a,b) -> {
            int scoreA = dist.get(a) == null ? Integer.MAX_VALUE : dist.get(a).score;
            int scoreB = dist.get(b) == null ? Integer.MAX_VALUE : dist.get(b).score;
            return scoreA - scoreB;
        });
        queue.add(grid.start);
        dist.put(grid.start, grid.spawnDeer());

        Point curr;
        while ((curr = queue.poll()) != null) {
            Grid16.Deer forward = dist.get(curr).clone();
            Grid16.Deer ccw = dist.get(curr).ccw();
            Grid16.Deer cw = dist.get(curr).cw();

            forward.prance();
            ccw.prance();
            cw.prance();

            if (dist.get(forward.location) == null || forward.score < dist.get(forward.location).score) {
                dist.put(forward.location, forward);
                queue.remove(forward.location);
                queue.add(forward.location);
            }
            if (dist.get(ccw.location) == null || ccw.score < dist.get(ccw.location).score) {
                dist.put(ccw.location, ccw);
                queue.remove(ccw.location);
                queue.add(ccw.location);
            }
            if (dist.get(cw.location) == null || cw.score < dist.get(cw.location).score) {
                dist.put(cw.location, cw);
                queue.remove(cw.location);
                queue.add(cw.location);
            }
        }

        return dist.get(grid.end).score;
    }

    public static int part2(Grid16 grid) {
        Map<Point,Grid16.Deer> dist = new HashMap<>();
        Map<Point,Set<Grid16.Deer>> prev = new HashMap<>();
        Queue<Point> queue = new PriorityQueue<>((a,b) -> {
            int scoreA = dist.get(a) == null ? Integer.MAX_VALUE : dist.get(a).score;
            int scoreB = dist.get(b) == null ? Integer.MAX_VALUE : dist.get(b).score;
            return scoreA - scoreB;
        });
        queue.add(grid.start);
        dist.put(grid.start, grid.spawnDeer());
        for (Point node : grid.nodes) {
            prev.put(node, new HashSet<>());
        }
        prev.get(grid.start).add(grid.spawnDeer());

        Point curr;
        while ((curr = queue.poll()) != null) {
            Grid16.Deer forward = dist.get(curr).clone();
            Grid16.Deer ccw = dist.get(curr).ccw();
            Grid16.Deer cw = dist.get(curr).cw();

            forward.prance();
            ccw.prance();
            cw.prance();

            if (dist.get(forward.location) == null || forward.score <= dist.get(forward.location).score + 1000) {
                prev.get(forward.location).add(forward);
                for (Grid16.Deer deer : prev.get(curr)) {
                    Grid16.Deer tmp = deer.clone();
                    tmp.setDirection(dist.get(curr).direction);
                    tmp.prance();
                    prev.get(forward.location).add(tmp);
                }

                if (dist.get(forward.location) == null || forward.score < dist.get(forward.location).score) {
                    dist.put(forward.location, forward);
                    queue.remove(forward.location);
                    queue.add(forward.location);
                    prev.put(forward.location, prev.get(forward.location).stream().filter(deer ->
                        deer.score <= forward.score + 1000
                    ).collect(Collectors.toSet()));
                }
            }

            if (dist.get(ccw.location) == null || ccw.score <= dist.get(ccw.location).score + 1000) {
                prev.get(ccw.location).add(ccw);
                for (Grid16.Deer deer : prev.get(curr)) {
                    Grid16.Deer tmp = deer.clone();
                    tmp.setDirection(dist.get(curr).direction.ccw());
                    tmp.prance();
                    prev.get(ccw.location).add(tmp);
                }

                if (dist.get(ccw.location) == null || ccw.score < dist.get(ccw.location).score) {
                    dist.put(ccw.location, ccw);
                    queue.remove(ccw.location);
                    queue.add(ccw.location);
                    prev.put(ccw.location, prev.get(ccw.location).stream().filter(deer ->
                        deer.score <= ccw.score + 1000
                    ).collect(Collectors.toSet()));
                }
            }

            if (dist.get(cw.location) == null || cw.score <= dist.get(cw.location).score + 1000) {
                prev.get(cw.location).add(forward);
                for (Grid16.Deer deer : prev.get(curr)) {
                    Grid16.Deer tmp = deer.clone();
                    tmp.setDirection(dist.get(curr).direction.cw());
                    tmp.prance();
                    prev.get(cw.location).add(tmp);
                }

                if (dist.get(cw.location) == null || cw.score < dist.get(cw.location).score) {
                    dist.put(cw.location, cw);
                    queue.remove(cw.location);
                    queue.add(cw.location);
                    prev.put(cw.location, prev.get(cw.location).stream().filter(deer ->
                        deer.score <= cw.score + 1000
                    ).collect(Collectors.toSet()));
                }
            }
        }

        return prev.get(grid.end).stream().filter(deer -> 
            deer.score == dist.get(deer.location).score
        ).map(deer -> deer.path()).reduce((a,b) -> {
            a.addAll(b);
            return a;
        }).get().size();
    }
}
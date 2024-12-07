import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class Code5A {
    public static class Graph implements Comparator<String> {
        private Map<String, HashSet<String>> adjacencies;

        public Graph() {
            adjacencies = new HashMap<>();
        }

        public void put(String from, String to) {
            if (!adjacencies.containsKey(from)) {
                adjacencies.put(from, new HashSet<>());
            }
            adjacencies.get(from).add(to);
        }

        public boolean containsEdge(String from, String to) {
            return adjacencies.containsKey(from) && adjacencies.get(from).contains(to);
        }

        public int compare(String a, String b) {
            if (a.equals(b)) {
                return 0;
            } else if (containsEdge(a, b)) {
                return 1;
            } else if (containsEdge(b, a)) {
                return -1;
            } else {
                return 0;
            }
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

        List<String[]> lists = new ArrayList<>();
        Graph graph = new Graph();
        for (String line : file) {
            if (line.contains("|")) {
                graph.put(line.split("\\|")[0], line.split("\\|")[1]);
            } else if (line.contains(",")) {
                lists.add(line.split(","));
            }
        }

        int res = 0;
        for (String[] row : lists) {
            if (IntStream.range(0, row.length-1).allMatch(i ->
                IntStream.range(i+1, row.length).allMatch(j ->
                    graph.compare(row[i], row[j]) >= 0
                )
            )) {
                res += Integer.parseInt(row[row.length / 2]);
            }
        }

        System.out.println(res);
    }
}
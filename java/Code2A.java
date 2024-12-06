import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code2A {
    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        Stream<List<Integer>> reports = file.stream().map((line) ->
            Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList())
        );

        System.out.println(reports.filter(Code2A::isSafe).count());
    }

    public static boolean isSafe(List<Integer> report) {
        List<Integer> diffs = IntStream.range(1, report.size()).map(i -> 
            report.get(i) - report.get(i-1)
        ).boxed().collect(Collectors.toList());
        return (diffs.stream().allMatch(i -> i > 0) || diffs.stream().allMatch(i -> i < 0)) && diffs.stream().allMatch(i -> Math.abs(i) <= 3);
    }
}
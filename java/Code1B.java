import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code1B {
    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        List<Integer> locA = new ArrayList<>();
        List<Integer> locB = new ArrayList<>();
        for (String line : file) {
            locA.add(Integer.parseInt(line.split("\s+")[0]));
            locB.add(Integer.parseInt(line.split("\s+")[1]));
        }

        int res = locA.stream().map(i ->
            i * Collections.frequency(locB, i)
        ).reduce(0, (a,b) -> a+b);

        System.out.println(res);
    }
}
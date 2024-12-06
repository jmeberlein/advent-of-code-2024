import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class Code3A {
    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        Pattern rgx = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        int res = file.stream().map(line ->
            rgx.matcher(line).results().map(match ->
                Integer.parseInt(match.group(1)) * Integer.parseInt(match.group(2))
            ).reduce(0, (a,b) -> a+b)
        ).reduce(0, (a,b) -> a+b);

        System.out.println(res);
    }
}
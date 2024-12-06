import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class Code3B {
    public static void main(String[] args) {
        List<String> file;
        try (
            BufferedReader in = new BufferedReader(new FileReader(args[0]))
        ) {
            file = in.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return;
        }

        int res = 0;
        boolean active = true;
        Pattern rgx = Pattern.compile("(do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\))");
        for (String line : file) {
            Matcher m = rgx.matcher(line);
            while (m.find()) {
                switch(m.group(0).substring(0,3)) {
                case "do(":
                    active = true;
                    break;
                case "don":
                    active = false;
                    break;
                default:
                    if (active) {
                        res += Integer.parseInt(m.group(2)) * Integer.parseInt(m.group(3));
                    }
                }
            }
        }

        System.out.println(res);
    }
}
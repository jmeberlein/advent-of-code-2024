import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;

public class Code7A {
    public static boolean check(List<Long> line) {
        long target;
        int index;
        Stack<Long> targetStack = new Stack<>();
        Stack<Integer> indexStack = new Stack<>();
        targetStack.push(line.get(0));
        indexStack.push(line.size()-1);
        while (!targetStack.empty()) {
            target = targetStack.pop();
            index = indexStack.pop();
            
            if (index == 1 && target == line.get(1)) {
                return true;
            } else if (index > 1) {
                if (target - line.get(index) >= line.get(1)) {
                    targetStack.push(target - line.get(index));
                    indexStack.push(index - 1);
                }
                if (target % line.get(index) == 0) {
                    targetStack.push(target / line.get(index));
                    indexStack.push(index - 1);
                }
            }
        }
        return false;
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

        long res = 0;
        for (String line : file) {
            List<Long> tok = Arrays.stream(line.split(":?\s+")).map(Long::parseLong).collect(Collectors.toList());
            if (check(tok)) {
                res += tok.get(0);
            }
        }

        System.out.println(res);
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class Code4B {
    public static class Grid {
        public List<String> grid;
        public final int rows, columns;

        public Grid(List<String> grid) {
            this.grid = grid;
            this.rows = grid.size();
            this.columns = grid.get(0).length();
        }

        public char get(int r, int c) {
            return grid.get(r).charAt(c);
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

        Grid grid = new Grid(file);
        int res = IntStream.range(1, grid.rows-1).map(r ->
            (int) IntStream.range(1, grid.columns-1).filter(c ->
                grid.get(r,c) == 'A' &&
                    ((grid.get(r+1,c+1) == 'M' && grid.get(r-1,c-1) == 'S') || (grid.get(r+1,c+1) == 'S' && grid.get(r-1,c-1) == 'M')) &&
                    ((grid.get(r+1,c-1) == 'M' && grid.get(r-1,c+1) == 'S') || (grid.get(r+1,c-1) == 'S' && grid.get(r-1,c+1) == 'M'))
            ).count()
        ).reduce(0, (a,b) -> a+b);
        System.out.println(res);
    }
}
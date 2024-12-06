import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class Code4A {
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
        int count = 0;
        int prevX, prevS;

        for (int r = 0; r < grid.rows; r++) {
            // If I just set it to -4, it will never be 3 characters off from another X/S
            prevX = prevS = -4;
            for (int c = 0; c < grid.columns; c++) {
                switch(grid.get(r,c)) {
                case 'X':
                    if (c - prevS == 3 && grid.get(r, c-1) == 'M' && grid.get(r, c-2) == 'A') {
                        count++;
                    }
                    prevX = c;
                    break;
                case 'S':
                    if (c - prevX == 3 && grid.get(r, c-1) == 'A' && grid.get(r, c-2) == 'M') {
                        count++;
                    }
                    prevS = c;
                    break;
                }
            }
        }

        for (int c = 0; c < grid.columns; c++) {
            // If I just set it to -4, it will never be 3 characters off from another X/S
            prevX = prevS = -4;
            for (int r = 0; r < grid.rows; r++) {
                switch(grid.get(r,c)) {
                case 'X':
                    if (r - prevS == 3 && grid.get(r-1, c) == 'M' && grid.get(r-2, c) == 'A') {
                        count++;
                    }
                    prevX = r;
                    break;
                case 'S':
                    if (r - prevX == 3 && grid.get(r-1, c) == 'A' && grid.get(r-2, c) == 'M') {
                        count++;
                    }
                    prevS = r;
                    break;
                }
            }
        }

        int maxI = grid.rows + grid.columns - 1;
        int maxJ;
        for (int i = 0; i < maxI; i++) {
            int r = Math.max(0, grid.rows - i - 1);
            int c = Math.max(0, i - grid.columns + 1);
            maxJ = Math.min(i+1, maxI-i);
            prevS = prevX = -4;
            for (int j = 0; j < maxJ; j++, r++, c++) {
                switch(grid.get(r,c)) {
                case 'X':
                    if (j - prevS == 3 && grid.get(r-1, c-1) == 'M' && grid.get(r-2, c-2) == 'A') {
                        count++;
                    }
                    prevX = j;
                    break;
                case 'S':
                    if (j - prevX == 3 && grid.get(r-1, c-1) == 'A' && grid.get(r-2, c-2) == 'M') {
                        count++;
                    }
                    prevS = j;
                    break;
                }
            }
        }

        for (int i = 0; i < maxI; i++) {
            int r = Math.max(0, i - grid.rows + 1);
            int c = Math.min(i, grid.columns - 1);
            maxJ = Math.min(i+1, maxI-i);
            prevS = prevX = -4;
            for (int j = 0; j < maxJ; j++, r++, c--) {
                switch(grid.get(r,c)) {
                case 'X':
                    if (j - prevS == 3 && grid.get(r-1, c+1) == 'M' && grid.get(r-2, c+2) == 'A') {
                        count++;
                    }
                    prevX = j;
                    break;
                case 'S':
                    if (j - prevX == 3 && grid.get(r-1, c+1) == 'A' && grid.get(r-2, c+2) == 'M') {
                        count++;
                    }
                    prevS = j;
                    break;
                }
            }
        }

        System.out.println(count);
    }
}
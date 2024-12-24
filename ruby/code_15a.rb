require 'matrix'

Grid = Struct.new(:grid) do
    def [](idx)
        if idx.class.method_defined?(:[])
            self.grid[idx[0]][idx[1]]
        else
            self.grid[idx]
        end
    end

    def []=(idx, val)
        if idx.class.method_defined?(:[])
            self.grid[idx[0]][idx[1]] = val
        else
            self.grid[idx] = val
        end
    end

    def map(&)
        self.grid.map(&)
    end

    def rows
        self.grid.count
    end

    def columns
        self.grid.first.count
    end
end

def print_grid(grid, robot)
    system 'clear'
    grid.map.with_index do |row, r|
        line = row.map.with_index do |col, c|
            if r == robot[0] && c == robot[1]
                '🤖'
            elsif col == :free
                '  '
            elsif col == :block
                '📦'
            elsif col == :wall
                '🧱'
            end
        end
        puts line.join
    end
    $stdin.gets
end

file = IO.foreach(ARGV[0]).map(&:strip)
cutoff = file.find_index('')
robot = nil
grid = Grid.new(file[...cutoff].map.with_index do |row, r|
    row.split('').map.with_index do |col, c|
        case col
        when '@'
            robot = Vector[r,c]
            :free
        when '.' then :free
        when 'O' then :block
        when '#' then :wall
        end
    end
end)
path = file[(cutoff+1)..].join

print_grid(grid, robot)
path.split('').map do |c|
    dir = case c
    when '^' then Vector[-1,0]
    when '>' then Vector[0,1]
    when 'v' then Vector[1,0]
    when '<' then Vector[0,-1]
    end

    case grid[robot+dir]
    when :wall
        nil
    when :free
        robot += dir
    when :block
        test = robot+dir
        test += dir while grid[test] == :block
        if grid[test] == :free
            grid[test] = :block
            grid[robot+dir] = :free
            robot += dir
        end
    end

    print_grid(grid, robot)
end

res = (0...grid.rows).sum do |r|
    (0...grid.columns).sum do |c|
        grid[[r,c]] == :block ? (100 * r + c) : 0
    end
end

# print_grid(grid, robot)
puts res

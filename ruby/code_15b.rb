require 'matrix'

class Grid
    attr_accessor :grid

    def initialize(grid)
        self.grid = grid
    end

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
            elsif col == :left
                '🔎'
            elsif col == :right
                '🔍'
            elsif col == :wall
                '🧱'
            end
        end
        puts line.join
    end
    $stdin.gets
end

def can_move?(grid, curr, dir)
    case grid[curr+dir]
    when :wall then false
    when :free then true
    when :left then can_move?(grid, curr+dir, dir) && can_move?(grid, curr + dir + Vector[0,1], dir)
    when :right then can_move?(grid, curr+dir, dir) && can_move?(grid, curr + dir + Vector[0,-1], dir)
    end
end

def move(grid, curr, dir)
    if grid[curr+dir] == :left
        move(grid, curr + dir, dir)
        move(grid, curr + dir + Vector[0,1], dir)
    elsif grid[curr+dir] == :right
        move(grid, curr + dir, dir)
        move(grid, curr + dir + Vector[0,-1], dir)
    end
    grid[curr+dir] = grid[curr]
    grid[curr] = :free
end

file = IO.foreach(ARGV[0]).map(&:strip)
cutoff = file.find_index('')
robot = nil
grid = Grid.new(file[...cutoff].map.with_index do |row, r|
    row.split('').map.with_index do |col, c|
        case col
        when '@'
            robot = Vector[r,c*2]
            [:free, :free]
        when '.' then [:free, :free]
        when 'O' then [:left, :right]
        when '#' then [:wall, :wall]
        end
    end.reduce(&:+)
end)

# print_grid(grid, robot)
file[(cutoff+1)..].join.split('').map do |c|
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
    when :left, :right
        # If the robot's moving left or right, the same algorithm as before still works
        if c == '<' || c == '>'
            test = robot+dir
            test += dir while grid[test] == :left || grid[test] == :right
            if grid[test] == :free
                until test == robot
                    grid[test] = grid[test - dir]
                    test -= dir
                end
                robot += dir
            end
        # Otherwise, we need to use recursion
        else
            if can_move?(grid, robot, dir)
                move(grid, robot, dir)
                robot += dir
            end
        end
    end

    # print_grid(grid, robot)
end

res = (0...grid.rows).sum do |r|
    (0...grid.columns).sum do |c|
        grid[[r,c]] == :left ? (100 * r + c) : 0
    end
end

# print_grid(grid, robot)
puts res

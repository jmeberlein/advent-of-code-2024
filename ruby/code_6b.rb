Point = Struct.new(:r, :c)

Guard = Struct.new(:loc, :dir) do
    def next
        case dir
        when :up
            Point.new(loc.r-1, loc.c)
        when :down
            Point.new(loc.r+1, loc.c)
        when :left
            Point.new(loc.r, loc.c-1)
        when :right
            Point.new(loc.r, loc.c+1)
        end
    end

    def step!
        case self.dir
        when :up
            loc.r -= 1
        when :down
            loc.r += 1
        when :left
            loc.c -= 1
        when :right
            loc.c += 1
        end
    end
end

def loop?(file, obstacle = nil)
    guard = Guard.new(Point.new(0, 0), :up)
    grid = Array.new(file.length + 2) { Array.new(file.first.length + 2, '.') }
    grid.map { |row| row[0] = row[-1] = '*' }
    (0...grid.first.length).map { |i| grid[0][i] = grid[-1][i] = '*' }
    (0...file.length).map do |r|
        (0...file.first.length).map do |c|
            case file[r][c]
            when '#'
                grid[r+1][c+1] = '#'
            when '^'
                grid[r+1][c+1] = 'X'
                guard.loc.r, guard.loc.c = r+1, c+1
            end
        end
    end

    return false if obstacle && obstacle.r == guard.loc.r && obstacle.c == guard.loc.c

    grid[obstacle.r+1][obstacle.c+1] = '#' if obstacle

    exited = false
    (4 * file.length * file.first.length).times do
        case grid[guard.next.r][guard.next.c]
        when '#'
            case guard.dir
            when :up
                guard.dir = :right
            when :right
                guard.dir = :down
            when :down
                guard.dir = :left
            when :left
                guard.dir = :up
            end
        when '*'
            exited = true
            break
        else
            guard.step!
            grid[guard.loc.r][guard.loc.c] = 'X'
        end
    end
    
    !exited
end

file = IO.foreach(ARGV[0]).map &:strip

res = (0...file.length).sum do |r|
    (0...file.first.length).count do |c|
        has_loop = loop?(file, Point.new(r, c))
        # Technically it's just debugging output, but it's a nice
        # indicator that the code's doing something
        puts "#{r}, #{c}" if has_loop
        has_loop
    end
end

puts res
require 'matrix'

directions = [
    Vector[0,1],
    Vector[0,-1],
    Vector[1,0],
    Vector[-1,0]
]

file = IO.foreach(ARGV[0]).map { |s| '#' + s.strip + '#' }
file.unshift('#' * file.first.length)
file.push('#' * file.first.length)
rows = file.count
columns = file.first.length

visited = Array.new(rows) { Array.new(columns, false) }

# (0...rows).map { |i| puts file[i].split('').zip(visited[i]).to_s }

res = 0
(1...(rows-1)).map do |r|
    (1...(columns-1)).map do |c|
        if !visited[r][c]
            stack = [Vector[r,c]]
            area = 0
            perimeter = 0
            plant = file[r][c]
            visited[r][c] = true

            until stack.empty?
                curr = stack.pop
                area += 1
                directions.map do |dir|
                    test = curr + dir
                    if file[test[0]][test[1]] == plant && !visited[test[0]][test[1]]
                        visited[test[0]][test[1]] = true
                        stack << test
                    elsif file[test[0]][test[1]] != plant
                        perimeter += 1
                    end
                end
            end
            res += area * perimeter

            # puts "Found region of plant #{plant} with area #{area} and perimeter #{perimeter}"
        end
    end
end

puts res
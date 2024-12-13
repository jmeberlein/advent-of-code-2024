require 'matrix'

card_dirs = [
    Vector[0,1],
    Vector[0,-1],
    Vector[1,0],
    Vector[-1,0]
]

all_dirs = [Vector[0,1], Vector[1,1], Vector[1,0], Vector[1,-1], Vector[0,-1], Vector[-1,-1], Vector[-1,0], Vector[-1,1]]

file = IO.foreach(ARGV[0]).map { |s| '#' + s.strip + '#' }
file.unshift('#' * file.first.length)
file.push('#' * file.first.length)
rows = file.count
columns = file.first.length

visited = Array.new(rows) { Array.new(columns, false) }

res = 0
(1...(rows-1)).map do |r|
    (1...(columns-1)).map do |c|
        if !visited[r][c]
            stack = [Vector[r,c]]
            area = 0
            corners = 0
            plant = file[r][c]
            visited[r][c] = true

            until stack.empty?
                curr = stack.pop
                area += 1

                adjacencies = all_dirs.map do |dir|
                    test = curr + dir
                    file[test[0]][test[1]] == plant
                end

                # Check convex corners
                corners += 1 if !adjacencies[0] && !adjacencies[2]
                corners += 1 if !adjacencies[2] && !adjacencies[4]
                corners += 1 if !adjacencies[4] && !adjacencies[6]
                corners += 1 if !adjacencies[6] && !adjacencies[0]

                # Check concave corners
                corners += 1 if adjacencies[0] && adjacencies[2] && !adjacencies[1]
                corners += 1 if adjacencies[2] && adjacencies[4] && !adjacencies[3]
                corners += 1 if adjacencies[4] && adjacencies[6] && !adjacencies[5]
                corners += 1 if adjacencies[6] && adjacencies[0] && !adjacencies[7]

                card_dirs.map do |dir|
                    test = curr + dir
                    if file[test[0]][test[1]] == plant && !visited[test[0]][test[1]]
                        visited[test[0]][test[1]] = true
                        stack << test
                    end
                end
            end
            res += area * corners
        end
    end
end

puts res
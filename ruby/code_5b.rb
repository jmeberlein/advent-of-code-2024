adjacencies = {}
lists = []

IO.foreach(ARGV[0]) do |line|
    case line
    when /(\d*)\|(\d*)/
        adjacencies[$1] = [] unless adjacencies.key?($1)
        adjacencies[$1] << $2
    else
        lists << line.strip.split(",") unless line == "\n"
    end
end

all_pages = adjacencies.to_a.flatten.sort.uniq

res = lists.sum do |row|
    # Passing a block creates distinct arrays, as opposed to references to the same array
    adjacency_matrix = Array.new(row.count) { Array.new(row.count, 0) }
    adjacencies.map do |k,v|
        if row.include?(k)
            v.filter { |i| row.include?(i) }.map { |i| adjacency_matrix[row.find_index(k)][row.find_index(i)] = 1 }
        end
    end
    
    (0...row.count).map do |k|
        (0...row.count).map do |i|
            (0...row.count).map do |j|
                adjacency_matrix[i][j] = 1 if adjacency_matrix[i][k] == 1 && adjacency_matrix[k][j] == 1
            end
        end
    end
    (0...row.count).map do |i|
        (0...row.count).map do |j|
            adjacency_matrix[j][i] = -1 if adjacency_matrix[i][j] == 1
        end
    end

    if row.each_cons(2).all? { |a,b| adjacency_matrix[row.find_index(a)][row.find_index(b)] == 1 }
        0
    else
        row.sort { |a,b| adjacency_matrix[row.find_index(a)][row.find_index(b)] }[row.count / 2].to_i
    end
end

puts res
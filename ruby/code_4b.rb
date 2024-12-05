file = []

IO.foreach(ARGV[0]) do |line|
    file << line.strip
end

count = (1...file.length-1).sum do |i|
    (1...file.first.length-1).count do |j|
        file[i][j] == 'A' && ((file[i+1][j+1] == 'M' && file[i-1][j-1] == 'S') || (file[i+1][j+1] == 'S' && file[i-1][j-1] == 'M')) &&
                ((file[i+1][j-1] == 'M' && file[i-1][j+1] == 'S') || (file[i+1][j-1] == 'S' && file[i-1][j+1] == 'M'))
    end
end

puts count
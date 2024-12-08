file = IO.foreach(ARGV[0]).map(&:strip)

antennas = {}
rows = file.count
columns = file.first.length

(0...rows).map do |r|
    (0...columns).map do |c|
        unless file[r][c] == '.'
            antennas[file[r][c]] = [] unless antennas.key?(file[r][c])
            antennas[file[r][c]] << [r, c]
        end
    end
end

antinodes = Set[]
antennas.map do |_,v|
    v.map do |r1, c1|
        v.map do |r2, c2|
            next if r1 == r2 && c1 == c2

            r3, c3 = r1, c1
            while r3 >= 0 && r3 < rows && c3 >= 0 && c3 < columns
                antinodes << [r3, c3]
                r3 += r1 - r2
                c3 += c1 - c2
            end
        end
    end
end

puts antinodes.count
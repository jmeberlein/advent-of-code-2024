file = IO.foreach(ARGV[0]).map(&:strip)

file_sys = []
blocks = (0...file[0].length).step(2).sum { |i| file[0][i].to_i }
read_pos_b = file[0].length - 1
write_pos = 0
write_file_a, write_file_b = 0, file[0].length / 2
write_blocks_b = file[0][read_pos_b].to_i

(0...file[0].length).map do |i|
    if (i % 2).zero?
        file[0][i].to_i.times do
            file_sys[write_pos] = write_file_a
            write_pos += 1
        end
        write_file_a += 1
    else
        file[0][i].to_i.times do
            if write_blocks_b.zero?
                read_pos_b -= 2
                write_blocks_b = file[0][read_pos_b].to_i
                write_file_b -= 1
            end
            file_sys[write_pos] = write_file_b
            write_pos += 1
            write_blocks_b -= 1
        end
    end

    break if i == read_pos_b
end

# puts file_sys.to_s
res = (0...blocks).sum do |i|
    i * file_sys[i]
end

puts res
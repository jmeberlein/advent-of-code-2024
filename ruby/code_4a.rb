file = []

IO.foreach(ARGV[0]) do |line|
    file << line.strip
end

def count_xmas(str, debug = false)
    count = str.scan(/XMAS/).count + str.scan(/SAMX/).count
    puts "#{str}: #{count}" if debug
    count
end

count_horiz = file.sum { |line| count_xmas(line) }
puts "Horizontal: #{count_horiz}"

count_vert  = (0...file.first.length).sum do |i|
    line = (0...file.length).map { |j| file[j][i] }.join("")
    count_xmas(line)
end
puts "  Vertical: #{count_vert}"

max_i = file.length + file.first.length - 1
count_diag_forward = (0...max_i).sum do |i|
    off_r, off_c = [0,file.length - i - 1].max, [0,i - file.first.length + 1].max
    max_j = [max_i - i, i + 1].min
    line = (0...max_j).map { |j| file[off_r+j][off_c+j] }.join
    count_xmas(line)
end
puts "Diag. For.: #{count_diag_forward}"

count_diag_backward = (0...max_i).sum do |i|
    off_r, off_c = [0,i - file.length + 1].max, [i,file.first.length - 1].min
    max_j = [max_i - i, i + 1].min
    line = (0...max_j).map { |j| file[off_r+j][off_c-j] }.join
    count_xmas(line)
end
puts "Diag. Back: #{count_diag_backward}"

puts "     Total: #{count_horiz + count_vert + count_diag_forward + count_diag_backward}"
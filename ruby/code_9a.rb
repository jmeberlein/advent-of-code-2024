file = IO.foreach(ARGV[0]).map(&:strip)

file_sys = []
curr_file = 0
insert_pos = 0
is_file = true
file[0].chars.map do |c|
    if is_file
        c.to_i.times do
            file_sys[insert_pos] = curr_file
            insert_pos += 1
        end
        curr_file += 1
        is_file = false
    else
        insert_pos += c.to_i
        is_file = true
    end
end

i, j = 0, file_sys.count - 1
while i != j
    j -= 1 while file_sys[j].nil?
    if file_sys[i].nil?
        file_sys[i], file_sys[j] = file_sys[j], nil
        j -= 1
    end
    i += 1
end

puts "Finished defragmenting hard drive!"

res = (0..j).sum do |i|
    i * file_sys[i]
end

puts res
puts file_sys.to_s
loc_a = []
loc_b = []

IO.foreach(ARGV[0]) do |line|
    a, b = line.split(' ')
    loc_a.append(a.to_i)
    loc_b.append(b.to_i)
end

res = loc_a.sum do |i|
    i * loc_b.count(i)
end

puts res
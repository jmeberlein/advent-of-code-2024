loc_a = []
loc_b = []

IO.foreach(ARGV[0]) do |line|
    a, b = line.split(' ')
    loc_a.append(a.to_i)
    loc_b.append(b.to_i)
end

loc_a.sort!
loc_b.sort!

res = (0...loc_a.count).sum do |i|
    (loc_a[i] - loc_b[i]).abs
end

puts res
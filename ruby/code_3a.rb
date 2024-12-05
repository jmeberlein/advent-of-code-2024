file = []

IO.foreach(ARGV[0]) do |line|
    file += line.scan(/mul\((\d{1,3}),(\d{1,3})\)/)
end

puts file.map { |a,b| a.to_i * b.to_i }.sum

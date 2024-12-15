require 'matrix'

file = IO.foreach(ARGV[0]).map(&:strip)
robots = []
file.map do |line|
    px, py, vx, vy = line.scan(/-?\d+/)
    robots << [Vector[px.to_i, py.to_i], Vector[vx.to_i, vy.to_i]]
end

destinations = robots.map do |p,v|
    dest = p + 100*v
    Vector[dest[0] % 101 - 50, dest[1] % 103 - 51]
end

quad1 = destinations.count { |v| v[0].positive? && v[1].positive? }
quad2 = destinations.count { |v| v[0].negative? && v[1].positive? }
quad3 = destinations.count { |v| v[0].negative? && v[1].negative? }
quad4 = destinations.count { |v| v[0].positive? && v[1].negative? }

puts quad1 * quad2 * quad3 * quad4
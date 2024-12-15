require 'matrix'

file = IO.foreach(ARGV[0]).map(&:strip)

# Apparently, case statements have enough scope that I need to "initialize" vectors A and B
a = b = nil
res = 0
file.map do |line|
    case line
    when /A: X\+(\d+), Y\+(\d+)/
        a = Vector[$1.to_i, $2.to_i]
    when /B: X\+(\d+), Y\+(\d+)/
        b = Vector[$1.to_i, $2.to_i]
    when /X=(\d+), Y=(\d+)/
        c = Vector[$1.to_i, $2.to_i] + Vector[10000000000000, 10000000000000]
        x1 = Matrix.columns([c,b]).det.to_f / Matrix.columns([a,b]).det
        x2 = Matrix.columns([a,c]).det.to_f / Matrix.columns([a,b]).det
        res += 3*x1 + x2 if (x1 % 1).zero? && (x2 % 1).zero?
    end
end

puts res.to_i
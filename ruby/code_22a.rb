require 'matrix'

file = IO.foreach(ARGV[0]).map(&:strip)

res = file.sum do |line|
    secret = line.to_i
    2000.times do
        secret = ((secret << 6) ^ secret) & 0xFFFFFF
        secret = ((secret >> 5) ^ secret) #& 0xFFFFFF
        secret = ((secret << 11) ^ secret) & 0xFFFFFF
    end
    secret
end

puts res

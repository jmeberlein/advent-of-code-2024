file = []

IO.foreach(ARGV[0]) do |line|
    file += line.scan(/(do\(\)|don't\(\)|mul\(\d{1,3},\d{1,3}\))/)
end

active = true
sum = 0

file.flatten.map do |tok|
    case tok
    when "do()"
        active = true
    when "don't()"
        active = false
    # Theoretically, this could just be an "else", but because Ruby even provides pseudovariables
    # for matching groups when using a regex in a case statement, I'm just doing that
    when /mul\((\d{1,3}),(\d{1,3})\)/
        sum += $1.to_i * $2.to_i if active
    end
end

puts sum
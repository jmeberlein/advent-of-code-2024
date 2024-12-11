file = IO.foreach(ARGV[0]).map(&:strip)

curr = {}
file[0].split(" ").map do |tok|
    if curr.key?(tok.to_i)
        curr[tok.to_i] += 1
    else
        curr[tok.to_i] = 1
    end
end

75.times do
    step = {}
    curr.map do |k,v|
        if k.zero?
            step[1] = (step[1] || 0) + v
        elsif k.digits.count % 2 == 0
            fact = 10**(k.digits.count / 2)
            step[k%fact] = (step[k%fact] || 0) + v
            step[k/fact] = (step[k/fact] || 0) + v
        else
            step[k*2024] = (step[k*2024] || 0) + v
        end
    end
    curr = step
end

puts curr.sum { |_,v| v }
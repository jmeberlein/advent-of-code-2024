file = IO.foreach(ARGV[0]).map &:strip

def check(line)
    stack = [[line[0], line.count - 1]]
    until stack.empty?
        target, idx = stack.pop
        if idx == 1
            return true if target == line[1]
        else
            stack << [target-line[idx], idx-1] if target - line[idx] >= line[1]
            stack << [target/line[idx], idx-1] if target % line[idx] == 0
            stack << [target/(10**(line[idx].digits.count)), idx-1] if target % (10**(line[idx].digits.count)) == line[idx]
        end
    end
    false
end

res = file.sum do |line|
    check(line.split(/:?\s+/).map(&:to_i)) ? line.split(/:?\s+/)[0].to_i : 0
end

puts res
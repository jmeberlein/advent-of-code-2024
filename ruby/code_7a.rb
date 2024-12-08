file = IO.foreach(ARGV[0]).map(&:strip)

def check(equation)
    stack = [[equation[0], equation.count - 1]]
    until stack.empty?
        target, idx = stack.pop
        if idx == 1
            return true if target == equation[1]
        else
            stack << [target - equation[idx], idx - 1] if target - equation[idx] >= equation[1]
            stack << [target / equation[idx], idx - 1] if target % equation[idx] == 0
        end
    end
    false
end

res = file.sum do |line|
    equation = line.split(/:?\s+/).map(&:to_i)
    check(equation) ? equation[0] : 0
end

puts res
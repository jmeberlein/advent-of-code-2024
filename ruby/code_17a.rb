require 'matrix'


file = IO.foreach(ARGV[0]).map(&:strip)

registers = {
    a: file[0].split(' ').last.to_i,
    b: file[1].split(' ').last.to_i,
    c: file[2].split(' ').last.to_i,
}
instructions = file.last.split(' ').last.split(',').map(&:to_i)


out = []
ptr = 0
while ptr < instructions.count
    opcode = instructions[ptr]; ptr += 1  # Normally I'd use [ptr++], but Ruby doesn't have ++
    operand = instructions[ptr]; ptr += 1 # Normally I'd use [ptr++], but Ruby doesn't have ++
    combo = case operand
    when 0..3 then operand
    when 4 then registers[:a]
    when 5 then registers[:b]
    when 6 then registers[:c]
    else nil
    end

    case opcode
    when 0 then registers[:a] >>= combo
    when 1 then registers[:b] ^= operand
    when 2 then registers[:b] = combo % 8
    when 3 then ptr = operand unless registers[:a].zero?
    when 4 then registers[:b] ^= registers[:c]
    when 5 then out << combo % 8
    when 6 then registers[:b] = registers[:a] >> combo
    when 7 then registers[:c] = registers[:a] >> combo
    end
end

puts out.map(&:to_s).join(',')
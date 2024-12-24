file = IO.foreach(ARGV[0]).map(&:strip)

or_gates = {}
xor_gates = {}
and_gates = {}

file.each do |line|
    case line
    when /(.{3}) AND (.{3}) -> (.{3})/
        and_gates[$3] = Set[$1, $2]
    when /(.{3}) OR (.{3}) -> (.{3})/
        or_gates[$3] = Set[$1, $2]
    when /(.{3}) XOR (.{3}) -> (.{3})/
        xor_gates[$3] = Set[$1, $2]
    end
end

# Any XOR gates must either be
#    x## XOR y##
# or
#    -> z##
bad_xors = file.filter do |gate|
    /[^xy]{3} XOR [^xy]{3} -> [^z]/.match?(gate)
end.map { |gate| gate[-3..] }

# Any OR or AND gates must output either z45 or an internal wire
bad_outputs = file.filter do |gate|
    / (OR|AND) .{3} -> z/.match?(gate) && !(/-> z45/.match?(gate))
end.map { |gate| gate[-3..] }

# Except for x00 XOR y00 -> z00, if an XOR gate takes x## or y## as an input,
# its output must feed into another XOR gate
bad_half_adders = file.filter do |gate|
    /[xy]\d{2} XOR [xy]\d{2} -> [^z]/.match?(gate)
end.map { |gate| gate[-3..] }.filter do |wire|
    xor_gates.none? { |_,gate| gate.include?(wire) }
end

# Except for x00 AND y00, AND gates must feed into OR gates
bad_ands = file.filter do |gate|
    /AND/.match?(gate) && !(/x00/.match?(gate))
end.map { |gate| gate[-3..] }.filter do |wire|
    or_gates.none? { |_,gate| gate.include?(wire) }
end

puts bad_xors.to_s
puts bad_outputs.to_s
puts bad_half_adders.to_s
puts bad_ands.to_s

puts (bad_ands + bad_half_adders + bad_outputs + bad_xors).uniq.sort.join(',')
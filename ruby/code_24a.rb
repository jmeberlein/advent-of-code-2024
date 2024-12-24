file = IO.foreach(ARGV[0]).map(&:strip)

cutoff = file.find_index("")
$wires = file[...cutoff].to_h do |line|
    res = line.split(': ')
    res[1] = res[1].to_i
    res
end
$gates = file[cutoff+1...].to_h do |line|
    case line
    when /(.{3}) AND (.{3}) -> (.{3})/ then [$3, [$1, :'&', $2]]
    when /(.{3}) OR (.{3}) -> (.{3})/  then [$3, [$1, :'|', $2]]
    when /(.{3}) XOR (.{3}) -> (.{3})/ then [$3, [$1, :'^', $2]]
    end
end

def get_wire(wire)
    return $wires[wire] if $wires.key?(wire)
    
    $wires[wire] = get_wire($gates[wire][0]).send($gates[wire][1], get_wire($gates[wire][2]))
end

output_wires = $gates.keys.filter { |wire| wire[0] == 'z' }.sort_by { |wire| -(wire[1..].to_i) }
puts output_wires.reduce(0) { |acc, val| (acc << 1) | get_wire(val) }

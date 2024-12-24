require 'matrix'

$numpad_keys = {
    '7' => Vector[-3,0], '8' => Vector[-3,1], '9' => Vector[-3,2],
    '4' => Vector[-2,0], '5' => Vector[-2,1], '6' => Vector[-2,2],
    '1' => Vector[-1,0], '2' => Vector[-1,1], '3' => Vector[-1,2],
                         '0' => Vector[0,1],  'A' => Vector[0,2],
}

$dpad_keys = {
                        '^' => Vector[0,1], 'A' => Vector[0,2],
    '<' => Vector[1,0], 'v' => Vector[1,1], '>' => Vector[1,2],
}

$dpad_offets = {}
$dpad_keys.map do |k1, v1|
    $dpad_offets[k1] = {}
    $dpad_keys.map do |k2, v2|
        offset = v2 - v1
        horiz = (offset[1].positive? ? ['>'] : ['<']) * offset[1].abs
        vert = (offset[0].positive? ? ['v'] : ['^']) * offset[0].abs
        $dpad_offets[k1][k2] = if offset[1].negative?
            if v1[0].zero? && v2[1].zero?
                vert + horiz + ['A']
            else
                horiz + vert + ['A']
            end
        elsif offset[1].positive?
            if v1[1].zero? && v2[0].zero?
                horiz + vert + ['A']
            else
                vert + horiz + ['A']
            end
        else
            vert + ['A']
        end
    end
end

$numpad_offets = {}
$numpad_keys.map do |k1, v1|
    $numpad_offets[k1] = {}
    $numpad_keys.map do |k2, v2|
        offset = v2 - v1
        horiz = (offset[1].positive? ? ['>'] : ['<']) * offset[1].abs
        vert = (offset[0].positive? ? ['v'] : ['^']) * offset[0].abs
        $numpad_offets[k1][k2] = if offset[1].negative?
            if v1[0].zero? && v2[1].zero?
                vert + horiz + ['A']
            else
                horiz + vert + ['A']
            end
        elsif offset[1].positive?
            if v1[1].zero? && v2[0].zero?
                horiz + vert + ['A']
            else
                vert + horiz + ['A']
            end
        else
            vert + ['A']
        end
    end
end

$cache = {}
def get_length(curr, step, depth)
    return $dpad_offets[curr][step].count if depth.zero?
    return $cache[[curr, step, depth]] unless $cache[[curr, step, depth]].nil?
    
    path = $dpad_offets[curr][step]
    res = get_length('A', path[0], depth-1)
    (1...path.count).each do |i|
        res += get_length(path[i-1], path[i], depth-1)
    end
    $cache[[curr, step, depth]] = res
end

file = IO.foreach(ARGV[0]).map(&:strip)

res = file.sum do |code|
    key = code.split('').each_cons(2).reduce($numpad_offets['A'][code[0]]) do |acc, val|
        acc + $numpad_offets[val[0]][val[1]]
    end
    tmp = key.each_cons(2).reduce(get_length('A', key[0], ARGV[1].to_i - 1)) do |acc, val|
        acc + get_length(val[0], val[1], ARGV[1].to_i - 1)
    end
    tmp * code[...-1].to_i
end

puts res
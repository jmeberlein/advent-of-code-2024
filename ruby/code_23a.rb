file = IO.foreach(ARGV[0]).map(&:strip)

$adjacencies = {}
file.each do |line|
    tok = line.split('-')
    $adjacencies[tok[0]] = [] unless $adjacencies.key?(tok[0])
    $adjacencies[tok[1]] = [] unless $adjacencies.key?(tok[1])
    $adjacencies[tok[0]] << tok[1]
    $adjacencies[tok[1]] << tok[0]
end

networks = Set[]
$adjacencies.keys.filter { |k| k[0] == 't' }.each do |k3|
    $adjacencies.each do |k2,v|
        v.each do |k1|
            if k1 != k2 && $adjacencies[k1].include?(k3) && $adjacencies[k2].include?(k3)
                networks << Set[k1,k2,k3]
            end
        end
    end
end

puts networks.count
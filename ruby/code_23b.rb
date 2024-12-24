file = IO.foreach(ARGV[0]).map(&:strip)

$adjacencies = {}
file.each do |line|
    tok = line.split('-')
    $adjacencies[tok[0]] = Set[] unless $adjacencies.key?(tok[0])
    $adjacencies[tok[1]] = Set[] unless $adjacencies.key?(tok[1])
    $adjacencies[tok[0]] << tok[1]
    $adjacencies[tok[1]] << tok[0]
end

$cliques = Set[]
$cache = {}
def bron_kerbosch(clique = Set[], candidates = $adjacencies.keys.to_set, excluded = Set[])
    if candidates.empty? && excluded.empty?
        $cliques << clique
        return
    end

    candidates.each do |candidate|
        bron_kerbosch(clique + [candidate], 
                      candidates.intersection($adjacencies[candidate]),
                      excluded.intersection($adjacencies[candidate]))
        candidates.delete(candidate)
        excluded.add(candidate)
    end
end

bron_kerbosch

max = Set[]
$cliques.each do |clique|
    max = clique if clique.count > max.count
end

puts max.to_a.sort.join(',')
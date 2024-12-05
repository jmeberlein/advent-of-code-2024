require_relative './graph'
require_relative './util'

graph = Graph.new
lists = []

IO.foreach(ARGV[0]) do |line|
    case line
    when /(\d*)\|(\d*)/
        graph.add($1, $2)
    else
        lists << line.strip.split(",") unless line == "\n"
    end
end

res = lists.sum do |row|
    subgraph = graph.subgraph(row).transitive_closure!

    if row.sorted? { |a,b| subgraph.cmp(a,b) }
        0
    else
        row.sort { |a,b| subgraph.cmp(a,b) }[row.count / 2].to_i
    end
end

puts res
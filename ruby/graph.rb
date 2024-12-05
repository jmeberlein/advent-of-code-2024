class Graph
    attr_reader :adjacencies

    def initialize
        @adjacencies = {}
    end

    def add(from, to)
        @adjacencies[to] = [] unless @adjacencies.key?(to)
        @adjacencies[from] = [] unless @adjacencies.key?(from)
        @adjacencies[from] << to unless @adjacencies[from].include?(to)
    end

    def include?(from, to)
        @adjacencies.key?(from) && @adjacencies[from].include?(to)
    end

    def cmp(a, b)
        if a == b
            0
        elsif include?(a, b)
            1
        else
            -1
        end
    end

    def dup
        res = Graph.new
        @adjacencies.map do |k,v|
            v.map { |el| res.add(k, el) }
        end
        res
    end

    def subgraph(keys)
        res = Graph.new
        @adjacencies.filter { |k,_| keys.include?(k) }.map do |k, v|
            v.filter { |el| keys.include?(el) }.map { |el| res.add(k, el) }
        end
        res
    end

    def transitive_closure
        res = self.dup
        res.transitive_closure!
    end

    def transitive_closure!
        @adjacencies.keys.map do |k|
            @adjacencies.keys.map do |i|
                @adjacencies.keys.map do |j|
                    add(i,j) if @adjacencies[i].include?(k) && @adjacencies[k].include?(j)
                end
            end
        end
        self
    end
end
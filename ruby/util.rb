class Array
    def sorted?
        (0...self.count-1).all? do |i|
            ((i+1)...self.count).all? do |j|
                yield(self[i], self[j]) >= 0
            end
        end 
    end
end
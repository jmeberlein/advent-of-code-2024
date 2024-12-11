require 'matrix'

file = IO.foreach(ARGV[0]).map(&:strip)

directions = [Vector[-1,0], Vector[1,0], Vector[0,-1], Vector[0,1]]
rows = file.count
columns = file.first.length

trailheads = Set[]
(0...rows).map do |r|
    (0...columns).map do |c|
        trailheads << Vector[r,c] if file[r][c] == '0'
    end
end

res = trailheads.sum do |trailhead|
    curr = [trailhead]
    ('1'..'9').map do |el|
        step = []
        curr.map do |vec|
            directions.map do |dir|
                test = vec + dir
                if test[0] >= 0 && test[0] < rows && test[1] >= 0 && test[1] < columns && file[test[0]][test[1]] == el
                    step << test
                end
            end
        end
        curr = step
    end
    curr.count
end

puts res
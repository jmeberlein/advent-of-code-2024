require 'matrix'

file = IO.foreach(ARGV[0]).map(&:strip)
robots = []
velocities = []
file.map do |line|
    px, py, vx, vy = line.scan(/-?\d+/)
    robots << Vector[px.to_i, py.to_i]
    velocities << Vector[vx.to_i, vy.to_i]
end

time = 0
while true
    if robots.count == robots.uniq.count
        system 'clear'
        puts "Time: #{time}s"
        (0...103).map do |y|
            (0...101).map do |x|
                print (robots.any? { |p| p == Vector[x,y] } ? '#' : '.') 
            end
            print "\n"
        end
        $stdin.gets
    end

    time += 1
    (0...robots.count).map do |i| 
        robots[i][0] = (robots[i][0] + velocities[i][0]) % 101
        robots[i][1] = (robots[i][1] + velocities[i][1]) % 103
    end
end

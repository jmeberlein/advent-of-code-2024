file = IO.foreach(ARGV[0]).map(&:strip)

keys = []
locks = []
(0...file.count).step(8).map do |i|
    tmp = [0, 0, 0, 0, 0]
    (1...6).each do |r|
        (0...5).each do |c|
            tmp[c] += 1 if file[i+r][c] == '#'
        end
    end
    if file[i][0] == '.'
        keys << tmp
    else
        locks << tmp
    end
end

res =  keys.product(locks).count do |key, lock|
    (0...5).all? { key[_1] + lock[_1] <= 5}
end
puts res
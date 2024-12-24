require 'matrix'

def get_prices(initial)
    idx = 0
    secret = initial
    curr = secret % 10
    prev1 = prev2 = prev3 = prev4 = nil
    prices = {}
    3.times do
        prev1, prev2, prev3, prev4 = curr, prev1, prev2, prev3
        secret = ((secret << 6) ^ secret) & 0xFFFFFF
        secret = ((secret >> 5) ^ secret) #& 0xFFFFFF
        secret = ((secret << 11) ^ secret) & 0xFFFFFF
        curr = secret % 10
    end

    1997.times do
        prev1, prev2, prev3, prev4 = curr, prev1, prev2, prev3
        secret = ((secret << 6) ^ secret) & 0xFFFFFF
        secret = ((secret >> 5) ^ secret) #& 0xFFFFFF
        secret = ((secret << 11) ^ secret) & 0xFFFFFF
        curr = secret % 10
        diffs = [prev3 - prev4, prev2 - prev3, prev1 - prev2, curr - prev1]
        unless prices.key?(diffs)
            prices[diffs] = curr
        end
    end

    prices
end

file = IO.foreach(ARGV[0]).map(&:strip)
cache = {}
all_quartets = file.sum(Set[]) do |line| 
    cache[line] = get_prices(line.to_i)
    cache[line].keys.to_set
end

max_price = 0
max_quartet = nil
all_quartets.each do |quartet|
    price = file.sum { |line| cache[line][quartet] || 0 }
    if price > max_price
        max_price = price
        max_quartet = quartet
    end
end

puts max_price, max_quartet.to_s
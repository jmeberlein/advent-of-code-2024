file = IO.foreach(ARGV[0]).map(&:strip)

$cache = {}
$towels = file.first.split(", ")
def count(str)
    $cache[str] ||= $towels.sum do |towel|
        if str == towel
            1
        elsif str.end_with?(towel)
            count(str[...(-towel.length)])
        else
            0
        end
    end
end

puts file[2..].sum { |s| count(s) }
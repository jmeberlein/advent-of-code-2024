reports = []

IO.foreach("input_2.txt") do |line|
    reports << line.split(' ').map(&:to_i)
end

def safe?(report)
    diffs = report.each_cons(2).map { |a,b| a - b }
    (diffs.all?(&:positive?) || diffs.all?(&:negative?)) && diffs.all? { |i| i.abs <= 3 }
end

puts reports.count { |report| safe?(report) }
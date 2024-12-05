reports = []

IO.foreach(ARGV[0]) do |line|
    reports << line.split(' ').map(&:to_i)
end

def safe?(report)
    diffs = report.each_cons(2).map { |a,b| a - b }
    (diffs.all?(&:positive?) || diffs.all?(&:negative?)) && diffs.all? { |i| i.abs <= 3 }
end

def nearly_safe?(report)
    return true if safe?(report)

    potential_reports = (0...report.count).map do |i|
        tmp = report.dup
        tmp.delete_at(i)
        tmp
    end

    potential_reports.any? { |report| safe?(report) }
end

# I'm fairly certain there's a O(n) way to do this, involving rules like checking
# how many positive or negative diffs there are. I was just having trouble working
# out the exact rules, and just did an exhaustive search instead

# def nearly_safe?(report)
#     diffs = report.each_cons(2).map { |a,b| a - b }
#     positive = diffs.count(&:positive?)
#     negative = diffs.count(&:negative?)
#     zero = diffs.count(&:zero?)

#     if zero > 1
#         false
#     elsif zero > 0 && positive > 0 && negative > 0
#         false
#     elsif positive > 1 && negative > 1
#         false
#     elsif positive == 0 || negative == 0
#         diffs.all? { |i| i.abs <= 3 }
#     else
#         idx = if positive == 1
#             diffs.find_index(&:positive?)
#         else
#             diffs.find_index(&:negative?)
#         end

#         report.delete_at(idx)
#         safe?(report)
#     end
# end

puts reports.count { |report| nearly_safe?(report) }

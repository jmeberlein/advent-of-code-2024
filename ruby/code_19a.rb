file = IO.foreach(ARGV[0]).map(&:strip)
regex = Regexp.new('^(' + file.first.gsub(", ", "|") + ')+$')
puts file[2..].count { |s| regex =~ s }

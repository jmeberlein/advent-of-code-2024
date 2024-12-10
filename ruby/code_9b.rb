file = IO.foreach(ARGV[0]).map(&:strip)

Node = Struct.new(:prev, :next, :id, :blocks, :has_moved)

head = tail = Node.new(nil, nil, 0, file[0][0].to_i, false)
(1...file[0].length).map do |i|
    unless file[0][i] == '0'
        tail.next = Node.new(tail, nil, (i%2).zero? ? i/2 : nil, file[0][i].to_i, false)
        tail = tail.next
    end
end

# out = []
# temp = head
# while temp
#     out << [temp.id, temp.blocks]
#     temp = temp.next
# end
# puts out.to_s

until tail == head
    tail = tail.prev while tail.id.nil? || tail.has_moved
    break if tail == head

    puts "Scanning block #{tail.id}" if tail.id % 25 == 0

    curr = head
    curr = curr.next while curr && curr != tail && (!curr.id.nil? || curr.blocks < tail.blocks)

    if curr && curr != tail
        curr.prev = Node.new(curr.prev, curr, tail.id, tail.blocks, true)
        curr.prev.prev.next = curr.prev
        tail.id = nil
        curr.blocks -= tail.blocks

        if tail.next && tail.next.id.nil?
            tail.blocks += tail.next.blocks
            tail.next = tail.next.next
            tail.next.prev = tail if tail.next
        end
        if tail.prev && tail.prev.id.nil?
            tail.blocks += tail.prev.blocks
            tail.prev = tail.prev.prev
            tail.prev.next = tail
        end
        if curr.blocks.zero?
            curr.prev.next = curr.next
            curr.next.prev = curr.prev
        end
    end
    tail = tail.prev

    # out = []
    # temp = head
    # while temp
    #     out << [temp.id, temp.blocks]
    #     temp = temp.next
    # end
    # puts out.to_s
end

file_sys = []
curr = head
while curr
    curr.blocks.times { file_sys << (curr.id || 0) }
    curr = curr.next
end
res = (0...file_sys.count).sum do |i|
    i * file_sys[i]
end

puts res
(require '[clojure.string :as str])

(defn read-file [name]
      (map (fn[line] (map parse-long (str/split line #" ")))
           (str/split-lines (slurp name))))

(defn count-if [pred arr] (count (filter pred arr)))

(defn valid [record]
    (and (or (every? pos? record) (every? neg? record))
         (every? #(<= (abs %) 3) record)))

(println
    (count-if #(valid %)
              (map (fn[record] (map #(- (second %) (first %)) 
                                    (partition 2 1 record))) 
                   (read-file "input_2.txt"))))

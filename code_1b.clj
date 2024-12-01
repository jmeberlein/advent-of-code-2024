(require '[clojure.string :as str])

(defn read-file [name]
    (apply mapv vector 
        (map 
            (fn[line] (map parse-long (str/split line #"   "))) 
            (str/split-lines (slurp name))
        )
    )
)

(println
    (reduce +
        (apply
            (fn[list_a list_b] 
                (map 
                    (fn[a] (* a (count (filter #(= % a) list_b))))
                    list_a
                )
            )
            (read-file "input_1.txt") 
        )
    )
)
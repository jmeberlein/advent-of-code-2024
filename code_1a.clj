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
            #(map (fn[a,b] (abs (- a b))) (sort %1) (sort %2))
            (read-file "input_1.txt") 
        )
    )
)

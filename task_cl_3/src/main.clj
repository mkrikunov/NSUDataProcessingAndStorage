(ns main)


(def coreNum (.availableProcessors (Runtime/getRuntime)))

(defn futureBlock
  [pred block]
  (->> block
       (filter pred)
       (doall)
       (future)))

(defn pFilter [pred seq]
  (let [blocks (partition-all coreNum seq)]
    (flatten (map deref (doall (map #(futureBlock pred %) blocks))))))


;(println (pFilter odd? (range 10)))
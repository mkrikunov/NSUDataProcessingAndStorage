;(def numOfPh 5)
(def numOfPh 6)
(def numOfForks numOfPh)
(def thinkingTime 100)
(def eatingTime 100)
(def mealCount 4)

(def startedTransactions (atom 0))
(def finishedTransactions (atom 0))


(defn philosopher [id left right]
  (let [leftFork (left :fork)
        rightFork (right :fork)]
    (new Thread
         (fn []
           (loop [i 0]
             (when (< i mealCount)
               (locking *out*
                 (println (str "Philosopher " id " began to think")))
               (Thread/sleep thinkingTime)
               (locking *out*
                 (println (str "Philosopher " id " finished thinking")))
               (dosync
                 (swap! startedTransactions inc)
                 (locking *out*
                   (println (str "Philosopher " id " tries to take forks")))
                 (alter leftFork inc)
                 (locking *out*
                   (println (str "Philosopher " id " took the left fork")))
                 (alter rightFork inc)
                 (locking *out*
                   (println (str "Philosopher " id " took the right fork")))

                 )
               (swap! finishedTransactions inc)
               (locking *out*
                 (println (str "Philosopher " id " ate")))
               (recur (inc i))))))))


(def forks
  (doall (map
           (fn [a] (-> {:fork (ref 0)}))
           (range numOfPh))))

(def philosophers
  (map (fn [i] (philosopher i
                            (nth forks i)
                            (nth forks (mod (+ i 1) numOfPh))))
       (range numOfPh))
  )



(defn start [philosophers]
  (doall (map #(. % (Thread/start)) philosophers)))

(start philosophers)


(Thread/sleep 2500)
(println "")
(println "Started transactions: " (deref startedTransactions))
(println "Finished transactions: " (deref finishedTransactions))
(println "Useless transactions: " (- (deref startedTransactions) (deref finishedTransactions)))

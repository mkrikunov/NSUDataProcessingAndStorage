(ns main)

; Решето Эратосфена:
(defn sieve [init]
  (cons (first init)
        (lazy-seq (sieve (filter #(not (= 0 (mod % (first init))))
                                 (rest init))))))

; Простые числа, начиная с двух:
(def primes (sieve (iterate inc 2)))



; Пример использования:
(println (take 3 primes))
(println (take 5 primes))
(println (take 10 primes))
(println (take 25 primes))
(println (take 100 primes))

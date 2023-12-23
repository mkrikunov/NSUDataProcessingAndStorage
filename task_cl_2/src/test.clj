(ns test
  (:use main)
  (:require [clojure.test :refer :all]))


(deftest unitTests
  (is (= 5 (nth primes 2)))
  (is (= 11 (nth primes 4)))
  (is (= 29 (nth primes 9)))
  (is (= 97 (nth primes 24)))
  ;is (= 541 (nth primes 100)))

  (is (= [2, 3, 5, 7, 11, 13, 17, 19, 23, 29] (take 10 primes)))

  (time (nth primes 1000))
  )


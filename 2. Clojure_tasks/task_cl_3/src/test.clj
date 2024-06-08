(ns test
  (:use main)
  (:require [clojure.test :refer :all]))

(deftest unitTest1
  (is (=
        (filter odd? (range 100))
        (pFilter odd? (range 100))
        ))
  )

(deftest unitTest2
  (is (=
        (filter odd? (range 0))
        (pFilter odd? (range 0))
        ))
  )

(deftest unitTest3
  (time (filter odd? (range 100)))
  (time (pFilter odd? (range 100)))
  )
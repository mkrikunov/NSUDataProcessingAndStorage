; Фильтрация по последнему символу в word:
(defn filterByLast [word alphabet]
  (filter #(not (= (last word) %)) alphabet))

; Создание слов:
(defn wordsCreation [word alphabet]
  (map #(str word %) (filterByLast (clojure.string/split word #"") alphabet)))

; Объединение слов:
(defn combineWords [words alphabet]
  (mapcat #(wordsCreation % alphabet) words))

; Создание всех возможных слов длины N
; без повторяющихся подряд символов:
(defn createAllWords [alphabet n]
  (reduce combineWords (repeat n alphabet)))



; Пример использования:
(println (createAllWords (list "a" "b" "c") 3))
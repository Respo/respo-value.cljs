
(ns respo-value.schema (:require [clojure.string :as string]))

(def a-hash-map {:b 2, :a 1})

(def a-list (list 1 2 3 4))

(def a-function (fn [x] (+ x 1)))

(def a-bool true)

(def a-hash-set (hash-set 1 2 3))

(def a-vector [1 2 3])

(def a-nested-hash-map {:b {:c 3, :d {:e 4}, :f 5}, :a 1})

(def a-nested-vector [1 2 [3 4 [5 6]] 7])

(def a-string "a string")

(def a-keyword :kywd)

(def a-mixed-data {:a [1 2 {:c "str"}]})

(def a-number 1)


(ns respo-value.schema (:require [clojure.string :as string]))

(def a-bool true)

(def a-function (fn [x] (+ x 1)))

(def a-hash-map {:a 1, :b 2})

(def a-hash-set (hash-set 1 2 3))

(def a-keyword :kywd)

(def a-list (list 1 2 3 4))

(def a-mixed-data {:a [1 2 {:c "str"}]})

(def a-nested-hash-map {:a 1, :b {:c 3, :d {:e 4}, :f 5}})

(def a-nested-vector [1 2 [3 4 [5 6]] 7])

(def a-number 1)

(def a-string "a string")

(def a-vector [1 2 3])

(def store {:states {}, :cursor []})

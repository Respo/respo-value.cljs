
(ns respo-value.comp.container
  (:require-macros [respo.macros :refer [defcomp cursor-> <> div span]])
  (:require [hsl.core :refer [hsl]]
            [respo-value.comp.value :refer [render-value]]
            [respo-value.schema :as schema]
            [respo.comp.space :refer [=<]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo.core :refer [create-comp]]
            [respo-value.style.layout :as layout]
            [respo-value.style.widget :as widget]))

(def style-section {:display "flex", :font-family "Verdana", :padding "8px 8px"})

(def data-table
  [["a nil:" nil]
   ["a number:" schema/a-number]
   ["a string:" schema/a-string]
   ["a keyword:" schema/a-keyword]
   ["a bool:" schema/a-bool]
   ["a function:" schema/a-function]
   ["a list:" schema/a-list]
   ["a vector:" schema/a-vector]
   ["a hash-set:" schema/a-hash-set]
   ["a nested vector:" schema/a-nested-vector]
   ["a hash-map:" schema/a-hash-map]
   ["a nested hash-map:" schema/a-nested-hash-map]
   ["a mixed data:" schema/a-mixed-data]
   ["an element" (div {} (div {:style style-section}) (=< 8 nil))]])

(def style-value {})

(defcomp
 comp-section
 (states hint value)
 (div
  {:style style-section}
  (span {:inner-text hint, :style widget/style-hint})
  (div {:style style-value} (render-value states value))))

(defcomp
 comp-container
 (store)
 (let [states (:states store)]
   (div
    {:style layout/container}
    (span {:attrs {}})
    (div
     {}
     (->> data-table
          (map-indexed
           (fn [idx pair] [idx (cursor-> idx comp-section states (first pair) (last pair))]))))
    (comp-inspect "States" states nil))))

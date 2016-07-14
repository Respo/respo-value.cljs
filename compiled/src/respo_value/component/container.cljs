
(ns respo-value.component.container
  (:require [hsl.core :refer [hsl]]
            [respo-value.component.value :refer [render-value]]
            [respo-value.schema :as schema]
            [respo.alias :refer [create-comp div span]]))

(def style-section
 {:padding "8px 8px", :display "flex", :font-family "Verdana"})

(def style-hint {:color (hsl 0 0 50), :width "240px"})

(def style-value {})

(defn render-section [hint value]
  (div
    {:style style-section}
    (span {:style style-hint, :attrs {:inner-text hint}})
    (div {:style style-value} (render-value value))))

(def data-table
 [["This is a nil:" nil]
  ["This is a number:" schema/a-number]
  ["This is a string:" schema/a-string]
  ["This is a keyword:" schema/a-keyword]
  ["This is a bool:" schema/a-bool]
  ["This is a function:" schema/a-function]
  ["This is a list:" schema/a-list]
  ["This is a vector:" schema/a-vector]
  ["This is a hash-set:" schema/a-hash-set]
  ["This is a nested vector:" schema/a-nested-vector]
  ["This is a hash-map:" schema/a-hash-map]
  ["This is a nested hash-map:" schema/a-nested-hash-map]
  ["This is a mixed data:" schema/a-mixed-data]])

(defn render [store]
  (fn [state mutate]
    (div
      {}
      (span {:attrs {}})
      (div
        {}
        (->>
          data-table
          (map-indexed
            (fn [index pair] [index (apply render-section pair)]))
          (into (sorted-map)))))))

(def comp-container (create-comp :container render))

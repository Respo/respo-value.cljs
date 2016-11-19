
(ns respo-value.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-value.comp.value :refer [render-value]]
            [respo-value.schema :as schema]
            [respo.comp.space :refer [comp-space]]
            [respo.alias :refer [create-comp div span]]
            [respo-value.style.layout :as layout]
            [respo-value.style.widget :as widget]))

(def style-section {:padding "8px 8px", :display "flex", :font-family "Verdana"})

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
   ["an element" (div {} (div {:style style-section}) (comp-space 8 nil))]])

(def style-value {})

(defn render-section [hint value]
  (div
   {:style style-section}
   (span {:style widget/style-hint, :attrs {:inner-text hint}})
   (div {:style style-value} (render-value value))))

(defn render [store]
  (fn [state mutate]
    (div
     {:style layout/container}
     (span {:attrs {}})
     (div
      {}
      (->> data-table (map-indexed (fn [index pair] [index (apply render-section pair)])))))))

(def comp-container (create-comp :container render))

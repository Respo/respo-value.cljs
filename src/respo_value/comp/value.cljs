
(ns respo-value.comp.value
  (:require-macros [respo.macros :refer [defcomp cursor-> <> div span]])
  (:require [hsl.core :refer [hsl]]
            [respo-value.style.widget :as widget]
            [respo-value.style.layout :as layout]
            [respo-value.style.decoration :as decoration]
            [respo.core :refer [create-comp]]))

(declare comp-map)

(declare render-children)

(declare render-fields)

(declare comp-list)

(declare comp-set)

(declare comp-vector)

(declare comp-value)

(defcomp comp-nil () (<> span "nil" widget/literal))

(defcomp comp-string (x) (<> span (pr-str x) widget/literal))

(defcomp comp-function () (<> span "fn" widget/literal))

(defcomp comp-keyword (x) (<> span (str x) widget/literal))

(defn toggle-folding [folded?] (fn [e d! m!] (m! (not folded?))))

(defcomp comp-number (x) (<> span (str x) widget/literal))

(defcomp comp-bool (x) (<> span (str x) widget/literal))

(defcomp
 comp-value
 (states x level)
 (let [level (or level 0)]
   (cond
     (nil? x) (comp-nil)
     (number? x) (comp-number x)
     (string? x) (comp-string x)
     (keyword? x) (comp-keyword x)
     (fn? x) (comp-function x)
     (or (= x true) (= x false)) (comp-bool x)
     (vector? x) (comp-vector states x level)
     (set? x) (comp-set states x level)
     (seq? x) (comp-list states x level)
     (map? x) (comp-map states x level)
     :else
       (div {:style widget/style-unknown, :attrs {:inner-text (str "unknown" (pr-str x))}}))))

(defcomp
 comp-vector
 (states x level)
 (let [folded? (if (some? (:data states)) (:data states) (> level 1))]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :event {:click (toggle-folding folded?)}}
      (<> span (str "[]~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row), :event {:click (toggle-folding folded?)}}
      (<> span (str "[]") widget/only-text)
      (render-children states *cursor* x level)))))

(defcomp
 comp-set
 (states x level)
 (let [folded? (if (some? (:data states)) (:data states) (>= level 1))]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :event {:click (toggle-folding folded?)}}
      (<> span (str "#{}~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row), :event {:click (toggle-folding folded?)}}
      (<> span (str "#{}") widget/only-text)
      (render-children states *cursor* x level)))))

(defcomp
 comp-list
 (states x level)
 (let [folded? (if (some? (:data states)) (:data states) (>= level 1))]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :event {:click (toggle-folding folded?)}}
      (<> span (str "'()~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row), :event {:click (toggle-folding folded?)}}
      (<> span (str "'()") widget/only-text)
      (render-children states *cursor* x level)))))

(defn render-fields [states *cursor* xs level]
  (div
   {:style (merge widget/style-children layout/column)}
   (->> xs
        (map-indexed
         (fn [index field]
           [index
            (div
             {:style layout/row}
             (comp-value states (first field) 0)
             (cursor-> index comp-value states (last field) (inc level)))])))))

(defn render-children [states *cursor* xs level]
  (div
   {:style (merge widget/style-children layout/column)}
   (->> xs
        (map-indexed
         (fn [index child] [index (cursor-> index comp-value states child (inc level))])))))

(defcomp
 comp-map
 (states x level)
 (let [folded? (if (some? (:data states)) (:data states) (>= level 1))]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :event {:click (toggle-folding folded?)}}
      (<> span (str "{}~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row), :event {:click (toggle-folding folded?)}}
      (<> span "{}" widget/only-text)
      (render-fields states *cursor* x level)))))

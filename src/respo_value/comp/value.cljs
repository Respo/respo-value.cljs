
(ns respo-value.comp.value
  (:require [hsl.core :refer [hsl]]
            [respo-value.style.widget :as widget]
            [respo-value.style.layout :as layout]
            [respo-value.style.decoration :as decoration]
            [respo.core :refer [defcomp <> div span list-> >>]]))

(declare comp-map)

(declare render-children)

(declare render-fields)

(declare comp-list)

(declare comp-set)

(declare comp-vector)

(declare comp-value)

(defcomp comp-bool (x) (<> span (str x) widget/literal))

(defcomp comp-function () (<> span "fn" widget/literal))

(defcomp comp-keyword (x) (<> span (str x) widget/literal))

(defcomp comp-nil () (<> span "nil" widget/literal))

(defcomp comp-number (x) (<> span (str x) widget/literal))

(defcomp comp-string (x) (<> span (pr-str x) widget/literal))

(defn render-fields [states xs level]
  (list->
   :div
   {:style (merge widget/style-children layout/column)}
   (->> xs
        (map-indexed
         (fn [index field]
           [index
            (div
             {:style layout/row}
             (comp-value states (first field) 0)
             (comp-value (>> states index) (last field) (inc level)))])))))

(defn render-children [states xs level]
  (list->
   :div
   {:style (merge widget/style-children layout/column)}
   (->> xs
        (map-indexed
         (fn [index child] [index (comp-value (>> states index) child (inc level))])))))

(defcomp
 comp-vector
 (states x level)
 (let [cursor (:cursor states)
       state (or (:data states) {:folded? (>= level 1)})
       folded? (:folded? state)]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span (str "[]~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span (str "[]") widget/only-text)
      (render-children states x level)))))

(defcomp
 comp-value
 (states x level)
 (let [level (or level 0)]
   (cond
     (nil? x) (comp-nil)
     (number? x) (comp-number x)
     (string? x) (comp-string x)
     (keyword? x) (comp-keyword x)
     (fn? x) (comp-function)
     (or (= x true) (= x false)) (comp-bool x)
     (vector? x) (comp-vector states x level)
     (set? x) (comp-set states x level)
     (seq? x) (comp-list states x level)
     (map? x) (comp-map states x level)
     :else
       (div {:style widget/style-unknown, :attrs {:inner-text (str "unknown" (pr-str x))}}))))

(defcomp
 comp-set
 (states x level)
 (let [cursor (:cursor states)
       state (or (:data states) {:folded? (>= level 1)})
       folded? (:folded? state)]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span (str "#{}~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span (str "#{}") widget/only-text)
      (render-children states x level)))))

(defcomp
 comp-map
 (states x level)
 (let [cursor (:cursor states)
       state (or (:data states) {:folded? (>= level 1)})
       folded? (:folded? state)]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span (str "{}~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span "{}" widget/only-text)
      (render-fields states x level)))))

(defcomp
 comp-list
 (states x level)
 (let [cursor (:cursor states)
       state (or (:data states) {:folded? (>= level 1)})
       folded? (:folded? state)]
   (if (and folded? (not (empty? x)))
     (div
      {:style (merge widget/structure decoration/folded),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span (str "'()~" (count x)) widget/only-text))
     (div
      {:style (merge widget/structure layout/row),
       :on-click (fn [e d!] (d! cursor (update state :folded? not)))}
      (<> span (str "'()") widget/only-text)
      (render-children states x level)))))


(ns respo-value.component.value
  (:require [hsl.core :refer [hsl]]
            [respo-value.style.widget :as widget]
            [respo-value.style.layout :as layout]
            [respo-value.style.decoration :as decoration]
            [respo.alias :refer [create-comp div span]]
            [respo.comp.text :refer [comp-text]]))

(declare comp-map)

(declare render-children)

(declare render-fields)

(declare render-value)

(declare comp-list)

(declare comp-set)

(declare comp-vector)

(def comp-nil
 (create-comp
   :number
   (fn [] (fn [state mutate!] (comp-text "nil" widget/literal)))))

(def comp-string
 (create-comp
   :string
   (fn [x] (fn [state mutate!] (comp-text (pr-str x) widget/literal)))))

(def comp-function
 (create-comp
   :function
   (fn [] (fn [state mutate!] (comp-text "fn" widget/literal)))))

(def comp-keyword
 (create-comp
   :keyword
   (fn [x] (fn [state mutate!] (comp-text (str x) widget/literal)))))

(defn toggle-folding [mutate!] (fn [e dispatch!] (mutate!)))

(def comp-number
 (create-comp
   :number
   (fn [x] (fn [state mutate!] (comp-text (str x) widget/literal)))))

(def comp-bool
 (create-comp
   :bool
   (fn [x] (fn [state mutate!] (comp-text (str x) widget/literal)))))

(def comp-vector
 (create-comp
   :vector
   (fn [x level] (>= level 1))
   not
   (fn [x level]
     (fn [folded? mutate!]
       (if (and folded? (not (empty? x)))
         (div
           {:style (merge widget/structure decoration/folded),
            :event {:click (toggle-folding mutate!)}}
           (comp-text (str "[]~" (count x)) widget/only-text))
         (div
           {:style (merge widget/structure layout/row),
            :event {:click (toggle-folding mutate!)}}
           (comp-text (str "[]") widget/only-text)
           (render-children x level)))))))

(def comp-set
 (create-comp
   :set
   (fn [x level] (>= level 1))
   not
   (fn [x level]
     (fn [folded? mutate!]
       (if (and folded? (not (empty? x)))
         (div
           {:style (merge widget/structure decoration/folded),
            :event {:click (toggle-folding mutate!)}}
           (comp-text (str "#{}~" (count x)) widget/only-text))
         (div
           {:style (merge widget/structure layout/row),
            :event {:click (toggle-folding mutate!)}}
           (comp-text (str "#{}") widget/only-text)
           (render-children x level)))))))

(def comp-list
 (create-comp
   :list
   (fn [x level] (>= level 1))
   not
   (fn [x level]
     (fn [folded? mutate!]
       (if (and folded? (not (empty? x)))
         (div
           {:style (merge widget/structure decoration/folded),
            :event {:click (toggle-folding mutate!)}}
           (comp-text (str "'()~" (count x)) widget/only-text))
         (div
           {:style (merge widget/structure layout/row),
            :event {:click (toggle-folding mutate!)}}
           (comp-text (str "'()") widget/only-text)
           (render-children x level)))))))

(defn render-value
  ([x] (render-value x 0))
  ([x level]
    (cond
      (nil? x) (comp-nil)
      (number? x) (comp-number x)
      (string? x) (comp-string x)
      (keyword? x) (comp-keyword x)
      (fn? x) (comp-function x)
      (or (= x true) (= x false)) (comp-bool x)
      (vector? x) (comp-vector x level)
      (set? x) (comp-set x level)
      (list? x) (comp-list x level)
      (map? x) (comp-map x level)
      :else (div
              {:style widget/style-unknown,
               :attrs {:inner-text "unknown"}}))))

(defn render-fields [xs level]
  (div
    {:style (merge widget/style-children layout/column)}
    (->>
      xs
      (map-indexed
        (fn [index field] [index
                           (div
                             {:style layout/row}
                             (render-value (first field) (inc level))
                             (render-value
                               (last field)
                               (inc level)))])))))

(defn render-children [xs level]
  (div
    {:style (merge widget/style-children layout/column)}
    (->>
      xs
      (map-indexed
        (fn [index child] [index (render-value child (inc level))])))))

(def comp-map
 (create-comp
   :map
   (fn [x level] (>= level 1))
   not
   (fn [x level]
     (fn [folded? mutate!]
       (if (and folded? (not (empty? x)))
         (div
           {:style (merge widget/structure decoration/folded),
            :event {:click (toggle-folding mutate!)}}
           (comp-text (str "{}~" (count x)) widget/only-text))
         (div
           {:style (merge widget/structure layout/row),
            :event {:click (toggle-folding mutate!)}}
           (comp-text "{}" widget/only-text)
           (render-fields x level)))))))

(declare render-value)

(declare render-children)

(declare render-fields)

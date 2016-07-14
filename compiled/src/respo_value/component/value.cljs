
(ns respo-value.component.value
  (:require [hsl.core :refer [hsl]]
            [respo-value.style.widget :as widget]
            [respo.alias :refer [create-comp div span]]))

(defn style-number []
  (merge widget/literal {:background-color (hsl 240 80 60)}))

(defn style-nil []
  (merge widget/literal {:background-color (hsl 240 80 0)}))

(defn style-string []
  (merge widget/literal {:background-color (hsl 140 80 40)}))

(defn style-keyword []
  (merge widget/literal {:background-color (hsl 200 60 80)}))

(defn style-bool []
  (merge widget/literal {:background (hsl 140 80 50)}))

(defn style-vector []
  (merge
    widget/literal
    {:background-color (hsl 0 20 80), :cursor "pointer"}))

(def style-char {:color (hsl 0 80 30), :pointer-events "none"})

(defn style-list []
  (merge
    widget/literal
    {:background-color (hsl 120 80 70), :cursor "pointer"}))

(defn style-map []
  (merge
    widget/literal
    {:background-color (hsl 230 50 80), :cursor "pointer"}))

(defn style-set []
  (merge
    widget/literal
    {:background-color (hsl 230 50 70), :cursor "pointer"}))

(defn style-function []
  (merge widget/literal {:background-color (hsl 30 50 80)}))

(defn style-unknown [] {})

(declare render-value)

(declare render-children)

(def nil-component
 (create-comp
   :number
   (fn []
     (fn [state mutate]
       (span {:style (style-nil), :attrs {:inner-text "nil"}})))))

(def function-component
 (create-comp
   :function
   (fn []
     (fn [state mutate]
       (span {:style (style-function), :attrs {:inner-text "fn"}})))))

(def number-component
 (create-comp
   :number
   (fn [x]
     (fn [state mutate]
       (span {:style (style-number), :attrs {:inner-text (str x)}})))))

(def string-component
 (create-comp
   :string
   (fn [x]
     (fn [state mutate]
       (span
         {:style (style-string), :attrs {:inner-text (pr-str x)}})))))

(def keyword-component
 (create-comp
   :keyword
   (fn [x]
     (fn [state mutate]
       (span {:style (style-keyword), :attrs {:inner-text (str x)}})))))

(def bool-component
 (create-comp
   :bool
   (fn [x]
     (fn [state mutate]
       (span {:style (style-bool), :attrs {:inner-text (str x)}})))))

(defn toggle-folding [mutate] (fn [simple-event dispatch] (mutate)))

(def vector-component
 (create-comp
   :vector
   (fn [x level] (> level 1))
   not
   (fn [x level]
     (fn [folded? mutate]
       (if folded?
         (div
           {:style (style-vector),
            :event {:click (toggle-folding mutate)}}
           (span
             {:style widget/only-text,
              :attrs {:inner-text (str "Vector:" (count x))}}))
         (div
           {:style (style-vector),
            :event {:click (toggle-folding mutate)}}
           (span {:style style-char, :attrs {:inner-text "["}})
           (span {:style style-char, :attrs {:inner-text "]"}})
           (render-children x level)))))))

(def set-component
 (create-comp
   :set
   (fn [x level] (> level 1))
   not
   (fn [x level]
     (fn [folded? mutate]
       (if folded?
         (div
           {:style (style-set),
            :event {:click (toggle-folding mutate)}}
           (span
             {:style widget/only-text,
              :attrs {:inner-text (str "Set:" (count x))}}))
         (div
           {:style (style-set),
            :event {:click (toggle-folding mutate)}}
           (span {:style style-char, :attrs {:inner-text "#{"}})
           (span {:style style-char, :attrs {:inner-text "}"}})
           (render-children x level)))))))

(def list-component
 (create-comp
   :list
   (fn [x level] (> level 1))
   not
   (fn [x level]
     (fn [folded? mutate]
       (if (not folded?)
         (div
           {:style (style-list),
            :event {:click (toggle-folding mutate)}}
           (span {:style style-char, :attrs {:inner-text "'("}})
           (span {:style style-char, :attrs {:inner-text ")"}})
           (render-children x level))
         (div
           {:style (style-list),
            :event {:click (toggle-folding mutate)}}
           (span
             {:style widget/only-text,
              :attrs {:inner-text (str "List:" (count x))}})))))))

(def map-component
 (create-comp
   :map
   (fn [x level] (> level 1))
   not
   (fn [x level]
     (fn [folded? mutate]
       (if folded?
         (div
           {:style (style-map),
            :event {:click (toggle-folding mutate)}}
           (span
             {:style widget/only-text,
              :attrs {:inner-text (str "Map:" (count x))}}))
         (div
           {:style (style-map),
            :event {:click (toggle-folding mutate)}}
           (span {:style style-char, :attrs {:inner-text "{"}})
           (span {:style style-char, :attrs {:inner-text "}"}})
           (render-children
             (->> x (map identity) (apply concat))
             level)))))))

(def style-children
 {:vertical-align "top", :padding "4px 4px", :display "inline-block"})

(defn render-children [xs level]
  (div
    {:style style-children}
    (->>
      xs
      (map-indexed
        (fn [index child] [index (render-value child (inc level))]))
      (into (sorted-map)))))

(defn render-value
  ([x] (render-value x 0))
  ([x level]
    (cond
      (nil? x) (nil-component)
      (number? x) (number-component x)
      (string? x) (string-component x)
      (keyword? x) (keyword-component x)
      (fn? x) (function-component x)
      (or (= x true) (= x false)) (bool-component x)
      (vector? x) (vector-component x level)
      (set? x) (set-component x level)
      (list? x) (list-component x level)
      (map? x) (map-component x level)
      :else (div
              {:style style-unknown, :attrs {:inner-text "unknown"}}))))

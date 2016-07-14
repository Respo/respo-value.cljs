
(ns respo-value.component.value
  (:require [hsl.core :refer [hsl]]
            [respo-value.style.widget :as widget]
            [respo.alias :refer [create-comp div span]]))

(declare render-value)

(declare render-children)

(def nil-component
 (create-comp
   :number
   (fn []
     (fn [state mutate]
       (span {:style widget/style-nil, :attrs {:inner-text "nil"}})))))

(def function-component
 (create-comp
   :function
   (fn []
     (fn [state mutate]
       (span
         {:style widget/style-function, :attrs {:inner-text "fn"}})))))

(def number-component
 (create-comp
   :number
   (fn [x]
     (fn [state mutate]
       (span
         {:style widget/style-number, :attrs {:inner-text (str x)}})))))

(def string-component
 (create-comp
   :string
   (fn [x]
     (fn [state mutate]
       (span
         {:style widget/style-string,
          :attrs {:inner-text (pr-str x)}})))))

(def keyword-component
 (create-comp
   :keyword
   (fn [x]
     (fn [state mutate]
       (span
         {:style widget/style-keyword,
          :attrs {:inner-text (str x)}})))))

(def bool-component
 (create-comp
   :bool
   (fn [x]
     (fn [state mutate]
       (span
         {:style widget/style-bool, :attrs {:inner-text (str x)}})))))

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
           {:style widget/style-vector,
            :event {:click (toggle-folding mutate)}}
           (span
             {:style widget/only-text,
              :attrs {:inner-text (str "Vector:" (count x))}}))
         (div
           {:style widget/style-vector,
            :event {:click (toggle-folding mutate)}}
           (span {:style widget/style-char, :attrs {:inner-text "["}})
           (span {:style widget/style-char, :attrs {:inner-text "]"}})
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
           {:style widget/style-set,
            :event {:click (toggle-folding mutate)}}
           (span
             {:style widget/only-text,
              :attrs {:inner-text (str "Set:" (count x))}}))
         (div
           {:style widget/style-set,
            :event {:click (toggle-folding mutate)}}
           (span {:style widget/style-char, :attrs {:inner-text "#{"}})
           (span {:style widget/style-char, :attrs {:inner-text "}"}})
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
           {:style widget/style-list,
            :event {:click (toggle-folding mutate)}}
           (span {:style widget/style-char, :attrs {:inner-text "'("}})
           (span {:style widget/style-char, :attrs {:inner-text ")"}})
           (render-children x level))
         (div
           {:style widget/style-list,
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
           {:style widget/style-map,
            :event {:click (toggle-folding mutate)}}
           (span
             {:style widget/only-text,
              :attrs {:inner-text (str "Map:" (count x))}}))
         (div
           {:style widget/style-map,
            :event {:click (toggle-folding mutate)}}
           (span {:style widget/style-char, :attrs {:inner-text "{"}})
           (span {:style widget/style-char, :attrs {:inner-text "}"}})
           (render-children
             (->> x (map identity) (apply concat))
             level)))))))

(defn render-children [xs level]
  (div
    {:style widget/style-children}
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
              {:style widget/style-unknown,
               :attrs {:inner-text "unknown"}}))))

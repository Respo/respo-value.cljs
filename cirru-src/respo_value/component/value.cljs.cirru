
ns respo-value.component.value $ :require
  [] hsl.core :refer $ [] hsl
  [] respo-value.style.widget :as widget

defn style-number ()
  merge widget/literal $ {}
    :background-color $ hsl 240 80 50

defn style-nil ()
  merge widget/literal $ {}
    :background-color $ hsl 240 80 0

defn style-string ()
  merge widget/literal $ {}
    :background-color $ hsl 140 80 40

defn style-keyword ()
  merge widget/literal $ {}
    :background-color $ hsl 200 60 80

defn style-bool ()
  merge widget/literal $ {}
    :background $ hsl 140 80 50

defn style-vector ()
  merge widget/literal $ {}
    :background-color $ hsl 0 20 80

def style-char $ {}
  :color $ hsl 0 80 30

defn style-list ()
  merge widget/literal $ {}
    :background-color $ hsl 120 80 70

defn style-map ()
  merge widget/literal $ {}
    :background-color $ hsl 230 50 80

defn style-set ()
  merge widget/literal $ {}
    :background-color $ hsl 230 50 60

defn style-function ()
  merge widget/literal $ {}
    :background-color $ hsl 30 50 80

defn style-unknown ()
  {}

declare render-value

declare render-children

def nil-component $ {} (:name :number)
  :update-state merge
  :get-state $ fn ()
    {}
  :render $ fn ()
    fn (state)
      [] :span $ {} (:style $ style-nil)
        :inner-text |nil

def function-component $ {} (:name :function)
  :update-state merge
  :get-state $ fn ()
    {}
  :render $ fn ()
    fn (state)
      [] :span $ {} (:style $ style-function)
        :inner-text |fn

def number-component $ {} (:name :number)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :span $ {} (:style $ style-number)
        :inner-text $ str x

def string-component $ {} (:name :string)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :span $ {} (:style $ style-string)
        :inner-text $ pr-str x

def keyword-component $ {} (:name :keyword)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :span $ {} (:style $ style-keyword)
        :inner-text $ str x

def bool-component $ {} (:name :bool)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :span $ {} (:style $ style-bool)
        :inner-text $ str x

def vector-component $ {} (:name :vector)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :div
        {} $ :style (style-vector)
        [] :span $ {} (:inner-text |[)
          :style style-char
        render-children x
        [] :span $ {} (:inner-text |])
          :style style-char

def set-component $ {} (:name :set)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :div
        {} $ :style (style-set)
        [] :span $ {} (:inner-text |#{)
          :style style-char
        render-children x
        [] :span $ {} (:inner-text |})
          :style style-char

def list-component $ {} (:name :list)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :div
        {} $ :style (style-list)
        [] :span $ {} (:style style-char)
          :inner-text "|'("
        render-children x
        [] :span $ {} (:style style-char)
          :inner-text "|)"

def map-component $ {} (:name :map)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :div
        {} $ :style (style-map)
        [] :span $ {} (:style style-char)
          :inner-text |{
        render-children $ ->> x (map identity)
          apply concat
        [] :span $ {} (:style style-char)
          :inner-text |}

defn render-children (xs)
  [] :div ({})
    ->> xs
      map-indexed $ fn (index child)
        [] index $ render-value child
      into $ sorted-map

defn render-value (x)
  cond
    (nil? x) ([] nil-component)
    (number? x) ([] number-component x)
    (string? x) ([] string-component x)
    (keyword? x) ([] keyword-component x)
    (or (= x true) (= x false)) ([] bool-component x)

    (vector? x) ([] vector-component x)
    (set? x) ([] set-component x)
    (list? x) ([] list-component x)
    (map? x) ([] map-component x)
    (fn? x) ([] function-component x)
    :else $ [] :div
      {} (:style style-unknown)
        :inner-text |unknown

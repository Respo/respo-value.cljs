
ns respo-value.component.value $ :require
  [] hsl.core :refer $ [] hsl

def style-number $ {}

def number-component $ {} (:name :number)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :div $ {} (:style style-number)
        :inner-text $ str x

def style-string $ {}

def string-component $ {} (:name :string)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :div $ {} (:style style-string)
        :inner-text $ pr-str x

def style-bool $ {}

def bool-component $ {} (:name :bool)
  :update-state merge
  :get-state $ fn (x)
    {}
  :render $ fn (x)
    fn (state)
      [] :div $ {} (:style style-bool)
        :inner-text $ str x

def style-unknown $ {}

defn render-value (x)
  cond
    (number? x) ([] number-component x)
    (string? x) ([] string-component x)
    (or (= x true) (= x false)) ([] bool-component x)

    :else $ [] :div
      {} (:style style-unknown)
        :inner-text |unknown

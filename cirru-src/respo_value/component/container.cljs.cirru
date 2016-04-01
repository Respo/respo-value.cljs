
ns respo-value.component.container $ :require
  [] hsl.core :refer $ [] hsl
  [] respo-value.component.value :refer $ [] render-value
  [] respo-value.schema :as schema

def style-section $ {}

def style-hint $ {}

def style-value $ {}

defn render-section (hint value)
  [] :div
    {} $ :style style-section
    [] :div $ {} (:style style-hint)
      :inner-text hint
    [] :div
      {} $ :style style-value
      render-value value

def container-component $ {} (:name :container)
  :get-state $ fn (store)
    {}
  :update-state $ fn (old-state)
    , old-state
  :render $ fn (store)
    fn (state)
      [] :div ({})
        [] :span $ {} (:inner-text |Container)
        render-section "|This is a number:" schema/a-number
        render-section "|This is a string:" schema/a-string
        render-section "|This is a bool:" schema/a-bool
        render-section "|This is a list:" schema/a-list
        render-section "|This is a vector:" schema/a-vector
        render-section "|This is a hash-map:" schema/a-hash-map

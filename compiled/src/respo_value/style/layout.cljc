
(ns respo-value.style.layout
  (:require [hsl.core :refer [hsl]]))

(def container {:padding "200px 24px"})

(def row
 {:align-items "flex-start", :display "flex", :flex-direction "row"})

(def column
 {:align-items "flex-start", :display "flex", :flex-direction "column"})

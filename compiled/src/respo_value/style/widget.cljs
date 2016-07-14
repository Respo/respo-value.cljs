
(ns respo-value.style.widget
  (:require [hsl.core :refer [hsl]]))

(def literal
 {:line-height "16px",
  :box-shadow (str "0 0 1px " (hsl 0 0 0 0.3)),
  :color (hsl 0 0 100),
  :vertical-align "top",
  :font-size "12px",
  :padding "0px 8px",
  :display "inline-block",
  :border-radius "4px",
  :font-family "Source Code Pro, menlo, monospace",
  :margin "0px 4px"})

(def only-text {:pointer-events "none"})

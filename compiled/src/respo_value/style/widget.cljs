
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

(def style-number (merge literal {:background-color (hsl 240 80 60)}))

(def style-nil (merge literal {:background-color (hsl 240 80 0)}))

(def style-string (merge literal {:background-color (hsl 140 80 40)}))

(def style-keyword (merge literal {:background-color (hsl 200 60 80)}))

(def style-bool (merge literal {:background (hsl 140 80 50)}))

(def style-vector
 (merge literal {:background-color (hsl 0 20 80), :cursor "pointer"}))

(def style-char {:color (hsl 0 80 30), :pointer-events "none"})

(def style-list
 (merge literal {:background-color (hsl 120 80 70), :cursor "pointer"}))

(def style-map
 (merge literal {:background-color (hsl 230 50 80), :cursor "pointer"}))

(def style-set
 (merge literal {:background-color (hsl 230 50 70), :cursor "pointer"}))

(def style-function (merge literal {:background-color (hsl 30 50 80)}))

(def style-unknown {})

(def style-children
 {:vertical-align "top", :padding "4px 4px", :display "inline-block"})

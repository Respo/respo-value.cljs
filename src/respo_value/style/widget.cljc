
(ns respo-value.style.widget (:require [hsl.core :refer [hsl]]))

(def structure
  {:line-height "16px",
   :box-shadow (str "0 0 1px " (hsl 0 0 0 0.3)),
   :color (hsl 0 0 40),
   :vertical-align "top",
   :font-size "12px",
   :cursor "pointer",
   :padding "0px 2px",
   :display "inline-block",
   :border-radius "4px",
   :font-family "Source Code Pro, menlo, monospace",
   :margin "4px"})

(def style-hint {:color (hsl 0 0 50), :font-size "14px", :width "240px"})

(def style-unknown {})

(def literal
  {:line-height "16px",
   :box-shadow (str "0 0 1px " (hsl 0 0 0 0.3)),
   :color (hsl 0 0 30),
   :vertical-align "top",
   :font-size "12px",
   :padding "0px 4px",
   :display "inline-block",
   :border-radius "4px",
   :font-family "Source Code Pro, menlo, monospace",
   :margin "4px"})

(def only-text {:pointer-events "none"})

(def style-children {:vertical-align "top", :padding "0px", :display "inline-block"})

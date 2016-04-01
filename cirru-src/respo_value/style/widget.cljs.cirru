
ns respo-value.style.widget $ :require
  [] hsl.core :refer $ [] hsl

def literal $ {} (:line-height |16px)
  :border-radius |4px
  :padding "|0 8px"
  :color $ hsl 0 0 100
  :font-family "|Source Code Pro, menlo, monospace"
  :font-size |12px
  :display |inline-block
  :margin "|0px 4px"
  :box-shadow $ str "|0 0 1px "
    hsl 0 0 0 0.3

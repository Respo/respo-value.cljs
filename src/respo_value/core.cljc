
(ns respo-value.core
  (:require [hsl.core :refer [hsl]]
            [respo.core :refer [render! clear-cache!]]
            [respo-value.component.container :refer [comp-container]]))

(defn dispatch! [op op-data] (.log js/console "dispatch:" op op-data))

(defonce store-ref (atom nil))

(defonce states-ref (atom {}))

(defn render-app! []
  (let [mount-target (.querySelector js.document "#app")]
    (comment println "states:" @states-ref)
    (render!
      (comp-container @store-ref)
      mount-target
      dispatch!
      states-ref)))

(defn on-jsload []
  (clear-cache!)
  (render-app!)
  (println "code updated."))

(defn -main []
  (enable-console-print!)
  (render-app!)
  (add-watch store-ref :rerender render-app!)
  (add-watch states-ref :rerender render-app!))

(set! js/window.onload -main)

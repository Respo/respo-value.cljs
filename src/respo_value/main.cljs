
(ns respo-value.main
  (:require [hsl.core :refer [hsl]]
            [respo.core :refer [render! clear-cache! realize-ssr!]]
            [respo.cursor :refer [mutate]]
            [respo-value.comp.container :refer [comp-container]]
            [respo-value.schema :as schema]))

(defonce *store (atom schema/store))

(defn dispatch! [op op-data]
  (if (= op :states)
    (swap! *store update :states (mutate op-data))
    (.log js/console "dispatch:" op op-data)))

(def mount-target (.querySelector js.document ".app"))

(defn render-app! [renderer] (renderer mount-target (comp-container @*store) dispatch!))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defn main! []
  (if ssr? (render-app! realize-ssr!))
  (render-app! render!)
  (add-watch *store :rerender (fn [] (render-app! render!))))

(defn reload! [] (clear-cache!) (render-app! render!) (println "Code updated."))


(ns respo-value.render
  (:require [respo.render.html :refer [make-string]]
            [shell-page.core :refer [make-page spit slurp]]
            [respo-value.comp.container :refer [comp-container]]
            [respo-value.schema :as schema]))

(def base-info {:title "Respo Value", :icon "http://logo.respo.site/respo.png", :ssr nil})

(defn dev-page []
  (make-page
   ""
   (merge
    base-info
    {:styles ["http://localhost:8100/main.css"],
     :scripts ["/main.js" "/browser/lib.js" "/browser/main.js"]})))

(defn prod-page []
  (let [html-content (make-string (comp-container schema/store))
        webpack-info (.parse js/JSON (slurp "dist/webpack-manifest.json"))
        cljs-info (.parse js/JSON (slurp "dist/cljs-manifest.json"))]
    (make-page
     html-content
     (merge
      base-info
      {:styles ["http://repo-cdn.b0.upaiyun.com/favored-fonts/main.css"
                (aget webpack-info "main.css")],
       :scripts [(-> cljs-info (aget 0) (aget "js-name"))
                 (-> cljs-info (aget 1) (aget "js-name"))]}))))

(defn main! []
  (if (= js/process.env.env "dev")
    (spit "target/index.html" (dev-page))
    (spit "dist/index.html" (prod-page))))

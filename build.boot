
(set-env!
 :asset-paths #{"assets"}
 :source-paths #{}
 :resource-paths #{"src"}

 :dev-dependencies '[]
 :dependencies '[[org.clojure/clojure       "1.8.0"       :scope "provided"]
                 [adzerk/boot-cljs          "1.7.170-3"   :scope "test"]
                 [adzerk/boot-reload        "0.4.6"       :scope "test"]
                 [mvc-works/boot-html-entry "0.1.1"       :scope "test"]
                 [cirru/boot-cirru-sepal    "0.1.1"       :scope "test"]
                 [org.clojure/clojurescript "1.8.40"      :scope "test"]
                 [binaryage/devtools        "0.5.2"       :scope "test"]
                 [mvc-works/respo           "0.1.9"       :scope "test"]
                 [mvc-works/respo-client    "0.1.9"       :scope "test"]
                 [mvc-works/hsl             "0.1.2"]])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[html-entry.core :refer [html-entry]]
         '[cirru-sepal.core :refer [cirru-sepal]])

(def +version+ "0.1.1")

(task-options!
  pom {:project     'mvc-works/respo-value
       :version     +version+
       :description "Workflow"
       :url         "https://github.com/mvc-works/respo-value"
       :scm         {:url "https://github.com/mvc-works/respo-value"}
       :license     {"MIT" "http://opensource.org/licenses/mit-license.php"}})

(set-env! :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"}]))

(defn html-dsl [data]
  [:html
   [:head
    [:title "Respo value"]
    [:link
     {:rel "stylesheet", :type "text/css", :href "style.css"}]
    [:link
     {:rel "icon", :type "image/png", :href "respo.png"}]
    [:style nil "body {margin: 0;}"]
    [:style
     nil
     "body * {box-sizing: border-box; }"]]
    [:script {:id "config" :type "text/edn"} (pr-str data)]
   [:body [:div#app] [:script {:src "main.js"}]]])

(deftask compile-cirru []
  (cirru-sepal :paths ["cirru-src"]))

(deftask dev []
  (comp
    (html-entry :dsl (html-dsl {:env :dev}) :html-name "index.html")
    (compile-cirru)
    (cirru-sepal :paths ["cirru-src"] :watch true)
    (watch)
    (reload :on-jsload 'respo-value.core/on-jsload)
    (cljs)))

(deftask build-simple []
  (comp
    (compile-cirru)
    (cljs)
    (html-entry :dsl (html-dsl {:env :dev}) :html-name "index.html")))

(deftask build-advanced []
  (comp
    (compile-cirru)
    (cljs :optimizations :advanced)
    (html-entry :dsl (html-dsl {:env :build}) :html-name "index.html")))

(deftask rsync []
  (fn [next-task]
    (fn [fileset]
      (sh "rsync" "-r" "target/" "tiye:repo/mvc-works/respo-value" "--exclude" "main.out" "--delete")
      (next-task fileset))))

(deftask send-tiye []
  (comp
    (build-advanced)
    (rsync)))

(deftask build []
  (comp
   (compile-cirru)
   (pom)
   (jar)
   (install)))

(deftask deploy []
  (comp
   (build)
   (push :repo "clojars" :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))

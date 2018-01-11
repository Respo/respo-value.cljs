
(defn read-password [guide]
  (String/valueOf (.readPassword (System/console) guide nil)))

(set-env!
  :resource-paths #{"src"}
  :dependencies '[[mvc-works/hsl   "0.1.2"]]
  :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"
                                     :username "jiyinyiyong"
                                     :password (read-password "Clojars password: ")}]))

(def +version+ "0.2.4")

(deftask deploy []
  (comp
    (pom :project     'respo/value
         :version     +version+
         :description "Respo value component"
         :url         "https://github.com/Respo/respo-value"
         :scm         {:url "https://github.com/Respo/respo-value"}
         :license     {"MIT" "http://opensource.org/licenses/mit-license.php"})
    (jar)
    (push :repo "clojars" :gpg-sign false)))

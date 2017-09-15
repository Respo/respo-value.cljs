
(set-env!
 :resource-paths #{"src"}
 :dependencies '[[respo           "0.3.32"]
                 [mvc-works/hsl   "0.1.2"]])

(def +version+ "0.2.0-alpha2")

(deftask build []
  (comp
    (pom :project     'respo/value
         :version     +version+
         :description "Respo value component"
         :url         "https://github.com/Respo/respo-value"
         :scm         {:url "https://github.com/Respo/respo-value"}
         :license     {"MIT" "http://opensource.org/licenses/mit-license.php"})
    (jar)
    (install)
    (target)))

(deftask deploy []
  (set-env!
    :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"}]))
  (comp
    (build)
    (push :repo "clojars" :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))

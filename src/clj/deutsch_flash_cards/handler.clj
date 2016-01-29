(ns deutsch-flash-cards.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [deutsch-flash-cards.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]))

(def mount-target
  [:div#app
    [:h3 "ClojureScript has not been compiled!"]
    [:p "please run "]
    [:b "lein figwheel"]
    " in order to start the compiler"])

(def loading-page
  (html
    [:html]
    [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1"}]
     (include-css (if (env :dev) "css/normalize.css" "css/normalize.min.css"))
     (include-css (if (env :dev) "css/skeleton.css"  "css/skeleton.min.css"))
     (include-css (if (env :dev) "css/app.css"       "css/app.min.css"))]
    [:body
     mount-target
     (include-js "js/app.js")]))


(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))

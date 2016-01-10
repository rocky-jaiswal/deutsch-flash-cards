(ns deutsch-flash-cards.core
  (:require [reagent.core :as reagent]
            [ajax.core :as ajax]))


;;------------------------
;; Atoms etc.

(defonce url "http://localhost:3449/phrases.json")

(defonce pointer (reagent/atom 0))

(defonce reveal  (reagent/atom "hidden"))

(defonce all-phrases (reagent/atom {}))

(defonce max-val (reagent/atom 0))

(defn phrase [index]
  (str "#" (inc index) (nth (keys @all-phrases) index)))

(defn description [index]
  (get @all-phrases (nth (keys @all-phrases) index)))


;;---------------------------
;; XHR Call

(defn handler [response]
  (reset! all-phrases response)
  (reset! max-val (count @all-phrases)))

(defn error-handler [{:keys [status status-text]}]
  (reset! all-phrases {:error true}))

(defn get-phrases []
  (ajax/GET url
            {:handler handler
             :error-handler error-handler
             :response-format :json
             :keywords? true}))

;;--------------------------
;; Actions

(defn show-next []
  (reset! reveal "hidden")
  (if (== @pointer (dec @max-val))
    (reset! pointer 0)
    (swap! pointer inc)))

(defn show-prev []
  (reset! reveal "hidden")
  (if (== @pointer 0)
    (reset! pointer (dec @max-val))
    (swap! pointer dec)))

(defn show-description []
  (if (= @reveal "hidden")
    (reset! reveal "visible")))

;;-------------------------
;; Components

(defn top-bar []
  [:div.top-bar
   [:div.container
    [:div.row
     [:h3 "Learn Deutsch"]]]])

(defn main []
  [:div.main
   [:h4 (phrase @pointer)]
   [:div.secondary {:class @reveal}
    [:h5 (description @pointer)]]])

(defn nav []
  [:div.nav
   [:button.button-primary {:on-click show-prev} "Prev"]
   [:button.button {:on-click show-description} "Show"]
   [:button.button-primary {:on-click show-next} "Next"]])

(defn app []
  [:div
   [top-bar]
   [:div.container
    [:div.row
     [main]
     [nav]]]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [app] (.getElementById js/document "app")))

(defn init! []
  (get-phrases)
  (mount-root))

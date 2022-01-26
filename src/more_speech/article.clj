(ns more-speech.article
  (:require [clojure.spec.alpha :as s])
  (:import (java.util Date)
           (java.text SimpleDateFormat)))

(s/def ::group string?)
(s/def ::subject string?)
(s/def ::author string?)
(s/def ::time number?)
(s/def ::body string?)
(s/def ::thread-count number?)
(s/def ::article (s/keys :req-un [::group ::subject ::author ::time ::body ::thread-count]))

(s/def ::author-nickname string?)
(s/def ::author-pubkey string?)
(s/def ::author-nickname-tuple (s/tuple ::author-pubkey ::author-nickname))

(defn make-article [name time body]
  {:group ""
   :author name
   :subject "?"
   :time time
   :body body
   :thread-count 1}
  )

(defn format-time [time]
  (let [time (* time 1000)
        date (Date. (long time))]
    (.format (SimpleDateFormat. "dd MMM yy kk:mm:ss z") date))
  )

(defn abbreviate [s n]
  (if (<= (count s) n)
    s
    (str (subs s 0 n) "...")))

(defn abbreviate-body [body]
  (abbreviate body 100))

(defn abbreviate-author [author]
  (abbreviate author 20))

(defn abbreviate-key [pubkey]
  (abbreviate pubkey 8))

(defn markup-article [article]
  [
   :bold
   (str "* " (abbreviate-author (:author article)))
   :regular
   (str " (" (:thread-count article) ")")
   :bold
   :pos 30
   (:subject article)
   :regular
   :pos 80
   (format-time (:time article))
   :new-line
   :multi-line (abbreviate-body (:body article))
   :line
   :new-line])

(defn markup-author [[pubkey name]]
  [:bold
   (abbreviate-key pubkey)
   :regular
   " - "
   name
   :new-line
   ])

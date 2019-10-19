(ns ratatouille.heating
  (:refer-clojure :exclude [println])
  (:import [clojure.lang PersistentQueue]))

(defn println [& args]
  (locking *out*
    (apply clojure.core/println args)))

(def -oven {:name           "one"
            :empty           true
            :status          :off
            :temperature     0
            :temperature-max 300
            :unit            :C})

(defmulti -convert (fn [quantity from to] [from to]))

(defn convert
  [quantity from to]
  (if (not= from to)
    (-convert quantity from to)
    quantity))

(defmethod -convert [:C :F] [c _ _] (+ (* c 9/5) 32))
(defmethod -convert [:F :C] [f _ _] (* (- f 32) 5/9))

(defn heat-up-oven
  [oven c]
  (let [max-temp (:temperature-max oven)]
    (println "Heating" oven "by 5 from" (:temperature oven))
    (update oven :temperature #(min max-temp c (+ % 5)))))

(heat-up-oven -oven 100)

(nth (iterate #(heat-up-oven % 100) -oven) 100)

(defn cool-down-oven
  [oven c]
  (println "Cooling" oven "by 5 from" (:temperature oven))
  (update oven :temperature #(max 0 c (- % 5))))

(def heat-sleep-ms 200)

(defn oven-behaviour [oven queue]
  (fn oven-behave! [_]
    (Thread/sleep heat-sleep-ms)
    (let [oven-agent *agent*]
      (dosync
        (send-off oven-agent oven-behave!)
        (if-let [cook (->> queue deref first)]
          (if (= (:status @oven) :off)
            (alter oven assoc :status :on)
            (let [[c p] cook
                  temperature (:temperature @oven)]
              (if (= temperature c)
                (when (not (realized? p))
                  (deliver p c))
                (if (< temperature c)
                  (alter oven heat-up-oven c)
                  (alter oven cool-down-oven c)))))
          (alter oven assoc :status :off))))))

(def tick-ms 100)

(defn universe-behaviour [oven]
  (fn universe! [_]
    (Thread/sleep tick-ms)
    (send-off *agent* universe!)
    (dosync
      (let [{:keys [status temperature name]} @oven]
        (when (and (> temperature 0) (= status :off))
          (alter oven cool-down-oven 0))))))

(defn heat-to [oven q f]
  (let [c (convert f :F (:unit @oven))
        p (promise)]
    (dosync
      (when (= (:status @oven) :off) (alter oven assoc :status :on))
      (commute q conj [c p])
      p)))

(defn ratatouille! [{:keys [oven queue]}]
  (let [is-hot (heat-to oven queue 375)]
    (deref is-hot)
    (println "Yummy!")))

(def oven       (ref -oven))
(def oven-queue (ref (PersistentQueue/EMPTY)))

(let [universe! (universe-behaviour oven)]
  (send-off (agent nil) universe!))

(let [oven-behave! (oven-behaviour oven oven-queue)]
  (send-off (agent nil) oven-behave!))

(def cook (ratatouille! {:oven oven
                         :queue oven-queue}))

(dosync
  (alter oven-queue pop))



(ns ratatouille.dining
  (:refer-clojure :exclude [println]))

(def n-philosophers 5)
(def txn-attempts (atom 0))
(def txn-success (ref 0))
(def running (atom true))
(def forks (for [_ (range n-philosophers)]
             (ref false)))

(defn println [& args]
  (locking *out*
    (apply clojure.core/println args)))

(declare get-forks eat think)
(defn philosopher [n]
  (while @running
    (when (get-forks n)
      (eat n)
      (think n)))
  (Thread/sleep 100))

(defn think [n]
  (println "Philosopher" n "is thinking...")
  (Thread/sleep 200))

(declare left-fork right-fork)
(defn get-forks [n]
  (let [l (left-fork n)
        r (right-fork n)]
    (dosync
      (swap! txn-attempts inc)
      (println "Philosopher" n "is trying to eat")
      (commute txn-success inc)
      (ref-set l true)
      (ref-set r true))))

(defn left-fork [n]
  (nth forks (mod (dec n) n-philosophers)))

(defn right-fork [n]
  (nth forks n))

(declare release-forks)
(defn eat [n]
  (println "Philosopher" n "is eating...")
  (Thread/sleep 200)
  (release-forks n))

(declare release-fork)
(defn release-forks [n]
  (dosync
    (release-fork (left-fork n))
    (release-fork (right-fork n))))

(defn release-fork [fork]
  (ref-set fork false))

(defn start! []
  (dotimes [i n-philosophers]
    (.start (Thread. #(philosopher i)))))

(do (reset! running true)
    (reset! txn-attempts 0)
    (dosync (ref-set txn-success 0))
    (dosync (dorun (map #(ref-set % false) forks)))
    (start!)
    (Thread/sleep 3000)
    (reset! running false))
@txn-attempts
@txn-success

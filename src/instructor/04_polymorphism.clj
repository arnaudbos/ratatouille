(ns instructor.04-polymorphism)

;; Polymorphic dispatch.
;; First we define the name of the multimethod, and the dispatch function:

(defmulti encounter
          (fn dispatch [x y]
            [(:species x) (:species y)]))

;; In this case the dispatch function returns a vector pair of
;; the species of input x and the species of input y.
;; Now we can provide methods implementing functions to execute for a given dispatch value:

(defmethod encounter [:bunny :lion] [x y] :run-away)
(defmethod encounter [:lion :bunny] [x y] :eat)
(defmethod encounter [:lion :lion] [x y] :fight)
(defmethod encounter [:bunny :bunny] [x y] :mate)

;; These are somewhere between a case statement and a function definition.
;; They give the conditions under which to be called, and a function definition.
;; Given a dispatch result of [:bunny :lion], the first method will be called on the x and y inputs,
;; and the method here does nothing but return a value :run-away.

;; Let’s set up some test inputs:

(def bunny1 {:species :bunny, :other :stuff})
(def bunny2 {:species :bunny, :other :stuff})
(def lion1 {:species :lion, :other :stuff})
(def lion2 {:species :lion, :other :stuff})

;; Now we can call encounter on the data to see what it does…​

(encounter bunny1 bunny2)
(encounter bunny1 lion1)
(encounter lion1 bunny1)
(encounter lion1 lion2)

;; Because keywords are functions, it’s quite common to use a keyword as a dispatch function.

(defmulti draw :shape)

;; Clojure offers other ways to express polymorphism, some of them
;; play well with Java interop, some others go beyond what' possible
;; to express ni Java's type system (expression problem).
;; The most common way after multimethods are named "Protocols", however
;; we con't cover them in this workshop.
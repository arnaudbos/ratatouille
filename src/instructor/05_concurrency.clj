(ns instructor.05-concurrency)

;; Vars are automatically derefed when evaluated, so it can seem like they are just a variable.
;; But you can “see” the var itself using the var function or #' shorthand.

(def one-hundred 100)

(var one-hundred)

(deref #'one-hundred)

;; The most common reason you would want to do that is to examine the metadata of a var:

(meta #'one-hundred)

;; Metadata may be provided using ^{}

(def ^{:private true} x 1)

(meta #'x)

;; You can attach whatever metadata you wish. These are the keys the compiler looks for:

:private
:doc
:author
:type

;; By default Vars are static. But Vars can be marked as dynamic to allow per-thread bindings.
;; Within each thread they obey a stack discipline:

(def ^:dynamic x 1)
(def ^:dynamic y 1)
(+ x y)

(binding [x 2 y 3]
  (+ x y))

(+ x y)

;; Bindings created with binding cannot be seen by any other thread.

;;; Atoms

;; Clojure separates concepts of state and identity.
;; A single identity can have different states over time,
;; but states themselves doesn't change, because they are immutable values.

;; Let's describe a user as a hash map.

(def user
  {:first-name "John"
   :last-name "Doe"
   :age 31})

;; Now if we want to update user's age, we have to perform the actual update
;; and get a new value back

(update user :age inc)
user

;; Since values are immutable, the result of an update should be stored somewhere
;; in order to keep track of it

;; In other words we want an identity that is represented by
;; one of those values at a single point in time

;; Atom is a special reference type (identity) that holds immutable value (state).
;; Atoms provide a way to manage shared, synchronous, independent state.

(def user' (atom {:first-name "John"
                  :last-name "Doe"
                  :age 31}))

;; We can read current state of the atom by dereferencing it (taking a value by reference)
(deref user')
@user'

;; Updating state of the atom is done in a functional manner
(swap! user' #(update % :age inc))

;; Or setting a new value directly
(reset! user' {:first-name "John"
               :last-name "Doe"
               :age 33})

;; Now a new value is stored in the atom
@user'

;; Since another thread may have changed the value in the intervening time,
;; it may have to retry, and does so in a spin loop.

;; Atoms are an efficient way to represent some state that will never need to
;; be coordinated with any other, and for which you wish to make
;; synchronous changes.

;;; Agents

;; Agents provide independent, asynchronous change of individual locations.
;; Agents are bound to a single storage location for their lifetime, and only allow mutation
;; of that location (to a new state) to occur as a result of an action.
;; Actions are functions (with, optionally, additional arguments) that are **asynchronously**
;; applied to an Agent’s state and whose return value becomes the Agent’s new state.

(def user'' (agent {:first-name "John"
                    :last-name "Doe"
                    :age 31}))

;; We can read current state of the agent by dereferencing it (taking a value by reference)
@user''

;; Updating the state of the agent is done by sending actions.
;; Actions are functions (with, optionally, additional arguments) that are asynchronously
;; applied to an Agent’s state and whose return value becomes the Agent’s new state.

(send user'' #(update % :age inc)) ; returns immediately
; Age still 31

;; Some time later:
@user''

;; Nested sends are dispatched only after the action succeeds
(defn level-up! [user]
  (Thread/sleep 500)
  (if (< (:age user) 40)
    (do
      (send-off *agent* level-up!)
      (update user :age inc))
    user))

(do
  (add-watch user'' :level-up
             (fn [key ref old new]
               (println "User age is now" (:age new))
               (when (= 40 (:age new))
                 (remove-watch ref key))))
  (send-off user'' level-up!))

;; Actions sent using `send` are dispatched in a fixed-size thread pool.
;; Hence better suited for CPU-bound tasks (actions should not block).

;; Actions sent using `send-off` are dispatched in a cached thread pool.
;; Hence better suited for IO-bound tasks (actions may block).

;; Agents are not actors!
;; From clojure.org ==> "Clojure’s Agents are reactive, not autonomous - there is no imperative message loop and no blocking receive."
;; * In-process only
;; * Point-in-time perception is free
;; * Send functions, not messages

;; Agents are integrated with the STM whereas Atoms aren't. But let's see `refs` first.

;;; Refs

;; Software Transactional Memory (STM for short) is still a research problem, so various implementations
;; exist (locking/pessimistic, lock-free/optimistic and all in-between).
;; While the advantages/disadvantages of STM is still discussed in academic literature, my personal opinion
;; is that those research focus on STM usability/practicality in object and/or "mutable" languages, while
;; Clojure's STM defeats many disadvantages simply because it relies heavily on its persistent
;; data structures and immutability semantics.

;; Transactional *ref*erences support coordinated, synchronous change of multiple locations.
(def user''' (ref {:first-name "John"
                   :last-name "Doe"
                   :age 31}))

;; We can read current state of the agent by dereferencing it (taking a value by reference)
@user'''

;; A little bit of boilerplate so we can actually read our log first
(defn safe-println [& more]
  (.write *out* (str (clojure.string/join " " more) "\n")))

;; Updating the state of the ref can only occur in a transaction (dosync).
(dotimes [x 10]
  (future
    (dosync
      (safe-println "Thread" x "trying to increment age from" (:age @user''') "to" (inc (:age @user''')))
      (alter user''' update :age inc))))

;; You will see a lot more logs than you'd expect.
;; Every transaction is *atomic* and *isolated*.
;; When conflict occurs, the transaction who 'lost' is retried and its effects are discarded.
;; This is why side effects must be avoided!

;; In the end, age has only been incremented ten times.
@user'''

;; For operations that are commutative, Clojure provides `commute` which provides more concurrency than `alter`:
(dotimes [x 10]
  (future
    (dosync
      (safe-println "Thread" x "trying to increment age from" (:age @user''') "to" (inc (:age @user''')))
      (commute user''' update :age inc))))

;; The result is consistent and we see only 10 logs this time.
;; The logs may be off though, because `commute` operation
;; are queued and may be executed multiple times too before succeeding
@user'''

;; There are more operations available on refs and more details about the STM than I could
;; deliver in a 1-day Clojure Workshop, so that's all for concurrency 🙂
(ns instructor.06-macros)

;;; The First Rule of Macro Club
;;; - You do not write macros.
;;; The Second Rule of Macro Club
;;; - You DO NOT write macros...
;;; The Third Rule of Macro Club
;;; - If this is your first time, you HAVE to write a macro.

;; Macros are expanded at compile time and allow users to extend the compiler.
;; Some functions you've seen in this workshop are actually macros.

(macroexpand '(->> {:age 31} :age inc range (map (partial % 2))))

(macroexpand '(when (< 31 40) (println "under 40") true))

;; Macros allow the language to remain small and grow "from the outside"
;; without requiring additions to the compiler itself.
;; Many libraries in Clojure provide new language features via macros.

;; Macros can also be used in user-code or libraries to avoid repeating code or boilerplate,
;; but beware of ad-hoc languages and DSLs that break composability.

;; Writing a macro equates to writing functions, except that the input and output data
;; are then used by the compiler as code to feed to the JVM.

;; When writing macros, don't think about evaluation or the value of parameters, think about
;; the code literally and use macroexpand a lot, it is your best friend.

;; Let's write our own if variant which prints "then" or "else" depending on
;; the successful branch.
;; We must emit code, and Clojure code are S-exprs, aka. lists,
;; so let's return code as data:

(defmacro pif
  [c a b]
  (list if c a b))

;; It fails because the REPL tries to evaluate if as a symbol, which is not bound, we must quote it first

(defmacro pif
  [c a b]
  (list 'if c a b))
(macroexpand '(pif true (inc 0) (dec 0)))
(macroexpand '(pif false (inc 0) (dec 0)))

;; Now we're making progress:

(defmacro pif
  [c a b]
  (list 'if c
        (do
          (println "then")
          a)
        (do
          (println "else")
          b)))
(macroexpand '(pif true "foo" "bar"))
(macroexpand '(pif false "foo" "bar"))

;; Evaluating macroexpand prints "then" and "else". This is not exactly what we want.
;; (do (println "then") a) is not quoted, so it evaluated as a function, not as data.

(defmacro pif
  [c a b]
  (list 'if c
        '(do
           (println "then")
           a)
        '(do
           (println "else")
           b)))

(macroexpand '(pif true "foo" "bar"))
(macroexpand '(pif false "foo" "bar"))

;; We can simplify this a bit using the backtick character; like this:

(defmacro pif
  [c a b]
  `(if c
     (do
       (println "then")
       a)
     (do
       (println "else")
       b)))

;; Let's try it:

(pif true "foo" "bar")

;; Ah, `c` is not bound here, what went wrong?

(macroexpand '(pif true "foo" "bar"))
(macroexpand '(pif false "foo" "bar"))

;; Yes, c was quoted, but here we don't want to quote it actually, we want the
;; data passed to the macro to be transcluded as is, so we need to unquote it specifically
;; (and a and b as well).

(defmacro pif
  [c a b]
  `(if ~c
     (do
       (println "then")
       ~a)
     (do
       (println "else")
       ~b)))

(macroexpand '(pif true "foo" "bar"))
(macroexpand '(pif false "foo" "bar"))

(pif true "foo" "bar")
(pif false "foo" "bar")

;; This example was very basic and in a real use case we would probably not use
;; a macro for this, especially when such behaviour can be implemented as a function
;; so easily.

;; There are more to macros than this, but for a brief introduction this will be enough.
;; If you wan to know more, go to:
;; - [Clojure for the Brave and True: Writing Macros](https://www.braveclojure.com/writing-macros/)
;; - [A "dead simple" introduction to Clojure macros](https://blog.brunobonacci.com/2015/04/19/dead-simple-introduction-to-clojure-macros/)
;; When I want to write a macro I usually go back to these pages (and others) because
;; I keep failing to remember the syntax of macros.

;; Another great resource to understand the power of macros is the implementation
;; of cooperative multitasking made in the Clojure core.async library.
;; This library brings lightweight concurrency mechanisms (aka. coroutines) to
;; Clojure without any modification to the language or Clojure's compiler.
;; - [The State Machines of core.async](http://hueypetersen.com/posts/2013/08/02/the-state-machines-of-core-async/)
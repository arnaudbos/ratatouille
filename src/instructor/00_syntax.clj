(ns instructor.00-syntax)

;;; Primitive data types

;; Clojure is relying on primitive data types provided by host platform
;; - Java's in JVM Clojure
;; - JavaScript's in ClojureScript
;; - .NET's in Clojure CLR

;; Strings are enclosed in double quotes
"Anyone can cook, but only the fearless can be great."
"Anyone can cook,
but only the fearless can be great."

;; Character literals are preceded by a backslash
\r \a \t \o \u \i \l \e \newline \tab

;; Numbers can be Long
1

;; Double
3.14

;; BigInteger, suffixed with N
1000000000000N

;; BigDecimal, suffixed with M
1000000000000.1M

;; Expressed as exponents
1e3

;; Or ratio
2/5

;; Numbers are automatically promoted if they overflow during arithmetic.

;; Booleans are represented as true and false.
true false

;; `nil` means nothing and is considered false in logical tests.
nil

;; Clojure code is made of S-expressions (symbolic expressions),
;; symbols enclosed with parentheses.

(+ 1 2) ;; <- `1 + 2` in most programming languages

;; In Lisps everything is an expression, there's no statements in the language.
;; The result of evaluation of the last expression is returned
(do
  (+ 1 2)   ;; <- evaluated but result is skipped
  (+ 1 3))  ;; <- returned

;; The first element of an S-expression is a function name
(inc 1)

;; or a special form (syntax)
(if true "Ratatouille" "Ghiveci")

;; or a macro
(defn f [x] x)

;; and all remaining elements are arguments.

(println "Anyone" "can" "cook," "but" "only" "the" "fearless" "can" "be" "great.")

;;; This is called “prefix notation” (as opposed to “infix notation” you're most probably familiar with).

;; Symbols are identifiers that are normally used to refer to something else.
;; They can be used in function parameters, let bindings (see later), class names and global vars.

;; Evaluating a symbol triggers runtime to find the value which is being referenced by the symbol.
println

foo

;; In case when we want a symbol itself, as a value, we have to quote it,
;; so the compiler knows that this symbol should be treated as data,
;; rather than a reference to a value in memory.

(quote foo)

;; Apostrophe is a syntactic shortcut for quote.
'foo

(def ratatouille "Ratatouille")

(string? ratatouille)

(symbol? 'ratatouille)

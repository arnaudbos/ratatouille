(ns instructor.01-datastructures)

;;; Collections: lists, vectors, maps, and sets

;; Clojure provides a rich set of immutable and persistent data structures,
;; which support efficient creation of “modified” versions, by utilizing structural sharing.
;; Don't know what "structural sharing" is? Please ask!

;; Collections are represented by abstractions, and there may be one or more concrete implementations.

;; In particular, since “modification” operations yield new collections,
;; the new collection might not have the same concrete type as the source collection,
;; but will have the same logical (interface) type.

;;; 1. Lists
;; Lists are forms enclosed in parentheses.

()

;; Lists are evaluated as function calls.

(inc 1)

;; Quote yields unevaluated forms.

(quote (1 2))
'(1 2)

;; List is a classic singly linked list data structure. Lisp code itself is made out of lists.
;; Lists support fast append to the beginning and retrieval of the head (first) element.

'(1 2 3)

(list? '(1 2 3))

(first '(1 2 3))
(conj '(1 2) 3)

;;; 2. Vectors
;; Vectors are enclosed in square braces

[1 2 3 4]

;; Vectors have order 1 (O(n)) lookup by index and count.
;; Vectors are used in preference to lists for cases where either could be used.
;; Vectors do not require quoting and are visually distinct.
;; Actually, you will rarely see or use lists.

(vector? [1 2 3])

(nth [1 2 3] 1)
(conj [1 2] 3)

;; Clojure compares by identity and by value. A vector with elements matching a list is equal to it.

(= [1 2 3] '(1 2 3))

;;; 3. Maps
;; A Map is a collection that maps keys to values.

{"Language" "Clojure"
 "Version" 1.10
 "Author" "Rich Hickey"}

;; Maps have near constant time lookup by key. (If you're wondering what "near constant time" means, just ask :))
;; Maps are tuned to be fast. Maps are an excellent replacement for object fields.

;; Keywords are shorthand identifiers that do not need to be declared. Keywords begin with a colon.

:language

;; Keywords are often used as keys in hashmaps; similar to fields in an object.

{:language "Clojure"
 :version 1.5
 :author "Rich Hickey"}

;; But keys can be of any type
{1      :number
 "1"    :string
 :a     :keyword
 [1]    :vector
 {:a 1} :map}

;; Keywords can be namespaced.

:instructor.01-datastructures/eggplant

;; Double colon is shorthand for a fully qualified keyword in the current namespace.

::eggplant

;;; 4. Sets
;; Sets are collections of unique values.

#{1 2 3}

;; Sets have near constant time membership lookup, with a high branching factor.

;; Collections can be combined and nested

{[1 2] {:name "diamond" :type :treasure}
 [3 4] {:name "dragon" :type :monster}}

;; This is a map that has vector coordinates as keys and maps as values.

;; Clojure has a sequence abstraction. Sequences can be lazy.
;; Their values are only created as they are consumed.

;; Lists and sequences are printed the same way.
(seq '(1 2 3))

;; Data structures are functions!

;; Maps, sets, vectors and keywords are functions. They delegate to get.
;; While it is possible to use get to access collections, calling the collection directly is more common.

(get {:a 1 :b 2} :a)

({:a 1 :b 2} :a)

(:a {:a 1 :b 2})

;; This is useful because you don’t need to create a function to call get.
;; It will prove itself useful when we'll see higher order functions.

;; Sets implement get:

(get #{1 2 3} 2)

(#{1 2 3} 2)

(remove #{nil "bad"} [:a nil :b "bad" "good"])

;; And so do vectors:

(get [1 2 3] 0)

([1 2 3] 0)

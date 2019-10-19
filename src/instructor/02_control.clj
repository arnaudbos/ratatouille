;;; Defining, binding and control flow

;; Namespace forms occur at the start of files.

(ns instructor.02-control
  (:require [clojure.string :as string]
            [clojure.set :refer [difference]]))

(string/upper-case "shout")

;; A var is used to store a mutable reference to a value.
;; Vars are unbound if no value is supplied.

(def x)

;; It is more common to supply an initial value.

(def x 1)

;; Def created a var named x which is bound to the value 1.
;; Vars are automatically dereferenced when evaluated.

;; Local variables are called local bindings in Clojure.
;; You can bind values to symbols and refer to their values in the scope of the binding.

(let [x 1]
  (inc x))

;; The symbol x is bound to the value 1, and the function inc is called on x, resulting in 2.

(let [x 1
      y 2
      x (+ x 1)]
  (+ x y)
  (* x x))

;; The binding scope is within the parentheses enclosing the let form, and will
;; shadow any existing bindings.

;; Destructuring is providing a literal data structure containing symbols that get bound
;; to the respective parts of a value with a matching structure.

(let [[x y] [1 2]]
  (+ x y))

;; Where we might otherwise bind the vector [1 2] to a single symbol,
;; here we destructure two symbols x and y by providing a pattern that matches the vector.

;; Destructuring is nested, so you can use it to pull out sub-values
;; without resorting to getter functions.

;; Common opportunities for destructuring are:

;; Values in a map:

(let [m {:field1 "foo" :field2 "bar"}]
  (println (:field1 m))
  (println (:field2 m)))

(let [{:keys [field1 field2]} {:field1 "foo" :field2 "bar"}]
  (println field1)
  (println field2))

;; Values in a sequence:

(let [v ["foo" "bar" "baz" "qux"]]
  (println (first v))
  (println (second v))
  (println (rest (rest v))))

(let [[foo bar & more] ["foo" "bar" "baz" "qux"]]
  (println foo)
  (println bar)
  (println more))

;; Nested destructuring

(let [x {:a {:b "foobar"}}]
  (get-in x [:a :b]))

(let [{{b :b} :a} {:a {:b "foobar"}}]
  b)

;; For more destructured goodness, see
;; [The complete guide to Clojure destructuring](https://blog.brunobonacci.com/2014/11/16/clojure-complete-guide-to-destructuring/).

;; Clojure provides special forms for control flow.
;; Special forms are built in primitives that behave differently from functions.

;; *if* chooses between two options.

(if (pos? 1)
  "one is positive"
  "or is it?")

;; Only one branch is evaluated, whereas a function call evaluates all arguments.

;; Often we want to execute some code only when a condition is met:

(when (pos? 1)
  (println "one is positive")
  (println "multiple expressions allowed"))

;; When the test fails, nothing is evaluated, when it passes, everything in the body is evaluated.

;; Cond allows for multiple branches.

(let [x {:cake 1}]
  (cond (= x 1) "one"
        (= x :cake) "the cake is a lie"
        (map? x) "it's a map!"
        :else "not sure what it is"))

;;; Functions

;; Functions are defined like this:

(defn square [x]
  (* x x))

;; All functions return a result, the result of the last expression in the form.
;; Defn binds the symbol square to a var which refers to a function which returns the result of multiplying
;; the input parameter x by itself.

(square 2)

;; When evaluated, a list containing square in the first position causes the var bound to square to be
;; automatically dereferenced to the function, which is called on the arguments.

;; Mathematical operators are regular functions which must be written in prefix notation.

(+ (square 2) (square 3))

;; Function arguments are evaluated from left to right before the function is called.

;; Unnamed functions are written as

(fn [a]
  (inc a))

;; Another way to make use of an anonymous function is to bind it in a let form:

(let [f (fn [x]
          (inc x))]
  (f 2))

;; Unnamed functions are also called anonymous functions and Lambda expressions.
;; There is a special syntax for creating unnamed functions.

#(inc %)

;; Is a function which increments a single argument.

(#(inc %) 1)

;; Unnamed functions created using this special syntax can have several arguments too.

(#(+ %1 %2) 1 1)

;; Closures are functions that capture values from the environment.

(let [who "world"
      f (fn greet []
          (str "Hello " who))]
  (f))

;; Note that function arguments are already a destructured vector.

;; Variadic functions are destructured using &.
;; Variadic means variable number of arguments.
;; Arity means number of arguments.

(defn sub [& vs]
  vs)

(sub 1 2 3 4)

;; Which produces a vector. Apply expands the vector arguments.
;; Most mathematical functions are variadic:

(+ 1 2 3)

;; In Clojure we often pass functions as values, so there is a convenient way to create
;; a function that consumes some arguments that can be used with additional arguments later:

(partial + 1)

;; Creates a function that adds 1 to any number of arguments supplied.
;; It returns a function that is equivalent to:

(fn [& args]
  (apply + 1 args))

;; So let’s see how we might make use of that:

((partial + 1) 2 3)

;; Functions are values and can be passed as arguments to other functions. Functions that take
;; a function as an argument are called higher order functions.

(defn greet []
  (println "Hello, world!"))

(defn higher-order-function [f]
  (f))

(higher-order-function greet)

;; Map is function that calls a function on every element in a sequence

(map #(inc %) [1 2 3])

;; Map is a higher order function because the first argument is a function.
;; Unnamed closures are useful as arguments to higher order functions.

(let [x 5]
  (map #(+ x %) [1 2 3]))

;; Here we have the symbol x bound to 5. We call the map function.
;; Our first argument is an unnamed function that captures x from the environment; a closure.
;; The closure is called on every element of the vector 1 2 3, resulting in a sequence 6 7 8.
;; Higher order functions, closures, and unnamed functions are terms that describe specific uses
;; of functions that allow concise expressions.

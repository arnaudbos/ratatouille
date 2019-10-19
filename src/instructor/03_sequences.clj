(ns instructor.03-sequences)

;; To really embrace Clojure is to think in terms of sequences and data structures.

;; The most basic way to construct a sequence is like so:

(cons 1 ())

(cons 3 (cons 2 (cons 1 ())))

;; But Clojure provides several easier ways to create a sequence:

(range 10)

;; Be careful though, Clojure can produce infinite sequences (don’t do this in a REPL):

(comment (range))

;; This would attempt to keep producing numbers forever.
;; There is a way to limit the amount of values to take:

(take 5 (range))

(take 5 (drop 5 (range)))

;; Clojure has an excellent sequence abstraction that fits naturally into the language.
;; From a vector [1 2 3 4] we can find the odd numbers by calling the filter function:

(filter odd? [1 2 3 4])

;; Here we called the filter function with two arguments: the odd? function and a vector of integers.
;; filter is a higher order function, since it takes an input function to use in its computation.
;; The result is a sequence of odd values.

;; Functions like filter that operate on sequences call seq on their arguments to convert collections to sequences.
;; The underlying mechanism is the ISeq interface, which allows many collection
;; data structures to provide access to their elements.

;; map is a function that applies another function for every element in a sequence:

(map inc [1 2 3 4])

;; The result is a sequence of the increment of each number in [1 2 3 4].

;;Remember, since keywords are functions, you don’t need to create a function to call get.

(map (fn [m] (get m :a)) [{:a 1} {:a 2} {:a 3}])

;; Can instead be written as:

(map :a [{:a 1} {:a 2} {:a 3}])

;; Where we are looking up the value associated with :a for each element in a vector of maps.

;; map can take multiple sequences from which to pull arguments for the input function:

(map +
     [1 3]
     [2 4])

;; Sequences can be used as input arguments to other functions as shown here:

(filter odd? (map inc [1 2 3 4]))

;; Here we filtered by odd? the values from (2 3 4 5), which was the result of calling map.

;; To aggregate across a sequence, use reduce:

(reduce * [1 2 3 4])

;; For each element in the sequence, reduce computes (* aggregate element) and passes
;; the result of that as the aggregate for the next calculation.
;; The first element 1 is used as the initial value of aggregate.
;; The final result is 1 * 2 * 3 * 4.

;; Clojure provides a built-in function for grouped aggregates:

(group-by count ["the" "quick" "brown" "fox"])

;; 3 letter words are "the" and "fox", whereas 5 letter words are "quick" and "brown".

;; filter is like a Java loop:

;; for (i=0; i < vector.length; i++)
;;   if (condition)
;;     result.append(vector[i]);

;; map is like a Java loop:

;; for (i=0; i < vector.length; i++)
;;   result[i] = func(vector[i]);

;; reduce is like a Java loop:

;; for (i=0; i < vector.length; i++)
;;   result = func(result, vector[i]);

;; Sequence abstractions are more concise and descriptive than loops,
;; especially when filtering multiple conditions, or performing multiple operations.

;; Clojure also has useful functions for constructing sequences:

(range 5)

(repeat 3 1)

(partition 3 (range 9))

;; Simple functions compose sequence operations together to build transforms.
;; Clojure has almost one hundred functions related to sequences, so you should
;; also be feeling wary of such dense code.
;; If we keep adding layers of function calls, the code becomes cryptic:

(reduce * (filter odd? (map inc [1 2 3 4 5])))

;; With three layers of function calls, things are getting hard to keep in our head all at once.
;; This expression may be easier to mentally process by starting from the innermost map,
;; working out to filter, and then out to reduce last.
;; But that is the opposite of our reading direction and locating the true starting point is difficult.

;; The presentation of sequence operations is clearer if you name intermediary results:

(let [incs (map inc [1 2 3 4 5])
      odd-incs (filter odd? incs)]
  (reduce * odd-incs))

;; Or use a thread last:

(->> [1 2 3 4 5]
     (map inc)
     (filter odd?)
     (reduce *))

;; Threading is good for unwrapping deeply nested function calls,
;; or avoiding naming intermediary steps that don’t have a natural name.

;; Thread first is similar, but passes the value in the first position

(-> 42 (/ 2) (inc))

;; Note that for empty expressions, the parenthesis are optional.

(-> 42 (/ 2) inc)

;;; Recursion

;; Functions that call themselves are called recursive. Here is an example of recursion:

(defn sum-up [coll result]
   (if (empty? coll)
     result
     (sum-up (rest coll) (+ result (first coll)))))
(sum-up [1 2 3] 10)

;; In Clojure there is a special way to do recursion which avoids consuming the stack (tail recursion):

(defn sum-up-with-recur [coll result]
   (if (empty? coll)
     result
     (recur (rest coll) (+ result (first coll)))))
(sum-up [1 2 3] 10)

;; Recur can only occur at the last position of a function (where scope can be discarded).

;;; Loops

;; Loop establishes bindings, and allows you to recur back to the start of the loop with new values.

(loop [a 0
       b 1]
   (if (< b 1000)
     (recur b (+ a b))
     a))

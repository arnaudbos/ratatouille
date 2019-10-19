;; this file should contain a vector of 2-vectors; each 2-vector must
;; contain a koan name and a map of koan answers, in that order.

[["00_syntax_01_equalities" {"__" [true
                                   2
                                   5
                                   true
                                   false
                                   true
                                   true
                                   false
                                   "hello"
                                   "hello"
                                   nil
                                   3]}]

 ["00_syntax_02_strings" {"__" ["hello"
                                "world"
                                "Cool "
                                "right?"
                                0
                                11
                                false
                                6 11
                                "123"
                                ", "
                                "1" "2" "3"
                                "olleh"
                                "hello"
                                13
                                nil
                                "hello world"
                                true
                                false
                                false
                                "a"
                                true
                                true
                                false]}]

 ["01_datastructures_01_lists" {"__" [1 2 3 4 5
                                      1
                                      [2 3 4 5]
                                      3
                                      0
                                      ()
                                      [:a :b :c :d :e]
                                      [:e :a :b :c :d]
                                      :a
                                      [:b :c :d :e]
                                      "No dice!"
                                      ()]}]

 ["01_datastructures_02_vectors" {"__" [1
                                        [1]
                                        [nil nil]
                                        2
                                        [111 222 333]
                                        :peanut
                                        :jelly
                                        :jelly
                                        [:butter :and]
                                        3]}]

 ["01_datastructures_03_sets" {"__" [[3]
                                     3
                                     #{1 2 3 4 5}
                                     #{1 2 3 4 5}
                                     #{2 3}
                                     #{1 4}]}]

 ["01_datastructures_04_maps" {"__" [:b 2
                                     1
                                     2
                                     2
                                     1
                                     1
                                     "Sochi"
                                     nil
                                     :key-not-found
                                     true
                                     false
                                     "February"
                                     1 "January"
                                     :c 3
                                     2
                                     2010 2014 2018
                                     "PyeongChang" "Sochi" "Vancouver"
                                     2 3]}]

 ["02_control_01_destructuring" {"__" [":bar:foo"
                                       (format (str "An Oxford comma list of %s, "
                                                    "%s, "
                                                    "and %s.")
                                               a b c)
                                       (apply str
                                              (interpose " "
                                                         (apply list
                                                                first-name
                                                                last-name
                                                                (interleave (repeat "aka") aliases))))
                                       {:original-parts full-name
                                        :named-parts {:first first-name :last last-name}}
                                       (str street-address ", " city ", " state)
                                       city state
                                       (str street-address ", " city ", " state)]
                                 "___" [(fn [[fname lname]
                                             {:keys [street-address city state]}]
                                          (str fname " " lname ", "
                                               street-address ", " city ", " state))]}]


 ["02_control_02_conditionals" {"__" [:a
                                      []
                                      nil
                                      :a
                                      :glory
                                      4 6 :your-road
                                      1
                                      :bicycling
                                      "is that even exercise?"]}]

 ["02_control_03_functions" {"__" [81
                                   20
                                   10
                                   60
                                   15
                                   "AACC"]
                             "___" [+
                                    *
                                    (fn [f] (f 5))
                                    (fn [f] (f 5))]}]

 ["02_control_04_creating_functions" {"__" [true false true
                                            4
                                            :a :b :c :d
                                            :c :d
                                            4
                                            8]
                                      "___" [(complement nil?)
                                             multiply-by-5
                                             (comp dec square)]}]

 ["03_sequences_01_lazy_sequences" {"__" [[1 2 3 4]
                                          [0 1 2 3 4]
                                          10
                                          95
                                          [1 2 4 8 16 32 64 128]
                                          :a]
                                    "___" [(fn [x] x)]}]

 ["03_sequences_02_sequence_comprehensions" {"__" [[0 1 2 3 4 5]
                                                   (* x x)
                                                   (range 10)
                                                   (odd? x) (* x x)
                                                   [row column]]}]


 ["03_sequences_03_higher_order_functions" {"__" [4 8 12
                                                  (* x x)
                                                  [false false true false false]
                                                  ()
                                                  [:anything :goes :here]
                                                  (< x 31)
                                                  (* 10 x) (< x 4)
                                                  24
                                                  100
                                                  (count a) (count b)]}]

 ["03_sequences_04_threading" {"__" [{:a 1}
                                     "Hello world, and moon, and stars"
                                     "String with a trailing space"
                                     6
                                     1
                                     [2 3 4]
                                     12
                                     [1 2 3]]}]

 ["03_sequences_05_recursion" {"__" [true
                                     acc
                                     (loop [coll coll
                                            acc ()]
                                       (if (seq coll)
                                         (recur (rest coll) (conj acc (first coll)))
                                         acc))
                                     (loop [n n
                                            acc 1]
                                       (if (zero? n)
                                         acc
                                         (recur (dec n) (* acc n))))]
                               "___" [not]}]

 ["04_polymorphism_01_runtime_polymorphism" {"__" [(str (:name a) " eats veggies.")
                                                   (str (:name a) " eats animals.")
                                                   (str "I don't know what " (:name a) " eats.")
                                                   "Hello World!"
                                                   "Hello, you silly world."
                                                   "Hello to this group: Peter, Paul, Mary!"]}]

 ["05_concurrency_01_atoms" {"__" [0
                                   1
                                   (swap! atomic-clock (partial + 4))
                                   20
                                   20
                                   atomic-clock 20 :fin]}]


 ["05_concurrency_02_refs" {"__" ["hello"
                                  "hello"
                                  "better"
                                  "better!!!"
                                  (dosync (ref-set the-world 0))
                                  (map :jerry [@the-world @bizarro-world])]

                            "___" [(fn [x] (+ 20 x))]}]

 ["06_macros" {"__" [~(first form)
                     ~(nth form 2)
                     form
                     (drop 2 form)
                     "Hello, Macros!"
                     10
                     '(+ 9 1)]}]]

;["19_datatypes" {"__" [(print
;                         (str "You're really the "
;                              (.category this)
;                              ", " recipient "... sorry."))
;                       "peace"
;                       "literature"
;                       "physics"
;                       nil
;                       [true false]
;                       (str "Congratulations on your Best Picture Oscar, "
;                            "Evil Alien Conquerors!")]}]
;
;["20_java_interop" {"__" [java.lang.String
;                          "SELECT * FROM"
;                          10
;                          1024]
;
;                    "___" [#(.toUpperCase %)]}]
;
;
;["21_partition" {"__" [partition
;                       [:a :b :c]
;                       '((0 1 2) (3 4))
;                       5
;                       :hello
;                       (6 :these :are)]}]
;
;
;["22_group_by" {"__" [odd?
;                      {5 ["hello" "world"] 3 ["foo" "bar"]}
;                      {1 [{:name "Bob" :id 1}
;                          {:last-name "Smith" :id 1}]
;                       2 [{:name "Jennifer" :id 2}]}
;                      nil
;                      {:naughty-list [{:name "Jimmy" :bad true}
;                                      {:name "Joe" :bad true}]
;                       :nice-list [{:name "Jane" :bad false}]}]}]
;
;["26_transducers" {"__" ['(2 3 4)
;                         [2 4]
;                         [2 4]
;                         [2 4]
;                         6]}]

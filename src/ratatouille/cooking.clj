(ns ratatouille.cooking)

(defmulti -convert (fn [quantity from to] [from to]))

(defn convert
  [quantity from to]
  (if (not= from to)
    (-convert quantity from to)
    quantity))

(def -fridge {:open false
              :temperature 5
              :unit :C
              :food {:vegetables {:eggplants [{:quantity 50 :unit :gram}
                                              {:quantity 100 :unit :gram}
                                              {:quantity 200 :unit :gram}
                                              {:quantity 200 :unit :gram}]
                                  :yellow-squashes [{:quantity 50 :unit :gram}
                                                    {:quantity 100 :unit :gram}
                                                    {:quantity 200 :unit :gram}]
                                  :zucchinis [{:quantity 50 :unit :gram}
                                              {:quantity 100 :unit :gram}
                                              {:quantity 200 :unit :gram}]
                                  :red-bell-pepper [{:quantity 50 :unit :gram}
                                                    {:quantity 100 :unit :gram}
                                                    {:quantity 200 :unit :gram}]
                                  :potatoes [{:quantity 50 :unit :gram}
                                             {:quantity 100 :unit :gram}
                                             {:quantity 100 :unit :gram}
                                             {:quantity 200 :unit :gram}]}
                     :salted-butter {:quantity 200 :unit :gram}
                     :unsalted-butter  {:quantity 200 :unit :gram}
                     :milk {:quantity 1000 :unit :milliliter}}})

(defmethod -convert [:gram :sprig] [g _ _] (* g 2))
(defmethod -convert [:sprig :gram] [s _ _] (* s 1/2))

(defmethod -convert [:gram :clove] [g _ _] (* g 1/6))
(defmethod -convert [:clove :gram] [c _ _] (* c 6))

(defn pick-vegetables
  [ingredient quantity unit]
  (reduce (fn [acc {q :quantity u :unit :as current}]
            (if (>= (:sum acc) quantity)
              (reduced acc)
              (-> acc
                  (update :sum #(+ % (convert q u unit)))
                  (update :ingredient (comp vec rest))
                  (update :picked conj current))))
          {:sum 0 :ingredient ingredient :picked []}
          ingredient))

(pick-vegetables [{:quantity 50 :unit :gram}
                  {:quantity 100 :unit :gram}
                  {:quantity 200 :unit :gram}]
                 150 :gram)

(defn adjust-vegetables-pick
  [{:keys [sum picked] :as p} quantity unit]
  (if (> sum quantity)
    (let [overflow (- sum quantity)
          u (:unit (peek picked))
          overflow-in-origin-unit (convert overflow unit u)]
      (-> p
          (assoc :sum quantity)
          ; put back overflow quantity into ingredient
          (update :ingredient #(vec (concat (if (> overflow-in-origin-unit 0) [{:quantity overflow-in-origin-unit :unit u}] []) %)))
          ; remove overflow from last picked
          (update :picked #(let [l (peek %)]
                             (conj (pop %) {:quantity (- (:quantity l) overflow) :unit (:unit l)})))))
    p))

(-> [{:quantity 50 :unit :gram}
     {:quantity 100 :unit :gram}
     {:quantity 200 :unit :gram}]
    (pick-vegetables 200 :gram)
    (adjust-vegetables-pick 200 :gram))

(defmulti pick-fridge-ingredient
          (fn [fridge type ingredient quantity unit]
            type))

(defn no-pick
  [key furniture]
  {key furniture
   :picked nil})

(defmethod pick-fridge-ingredient :vegetables
  [fridge _ name quantity unit]
  (if-let [ingredient (when (>= quantity 0) (get-in fridge [:food :vegetables name]))]
    (let [pick-result (pick-vegetables ingredient quantity unit)
          adjusted-pick (adjust-vegetables-pick pick-result quantity unit)]
      (if (and (> quantity 0) (= (:sum adjusted-pick) quantity))
        {:fridge (assoc-in fridge [:food :vegetables name] (:ingredient adjusted-pick))
         :picked (reduce #(update %1 :quantity + (:quantity %2)) (:picked adjusted-pick))}
        (no-pick :fridge fridge)))
    (no-pick :fridge fridge)))

(pick-fridge-ingredient -fridge :vegetables :eggplants -1 :gram)
(pick-fridge-ingredient -fridge :vegetables :eggplants 0 :gram)
(pick-fridge-ingredient -fridge :vegetables :eggplants 150 :gram)
(pick-fridge-ingredient -fridge :vegetables :eggplants 200 :gram)
(pick-fridge-ingredient -fridge :vegetables :eggplants 350 :gram)
(pick-fridge-ingredient -fridge :vegetables :eggplants 351 :gram)
(pick-fridge-ingredient -fridge :vegetables :duh 351 :gram)

(defmethod pick-fridge-ingredient :default
  [fridge _ name quantity unit]
  (if-let [ingredient (when (>= quantity 0) (get-in fridge [:food name]))]
    (let [ingredient-quantity (convert quantity unit (:unit ingredient))
          rest (- (:quantity ingredient) ingredient-quantity)]
      (if (>= rest 0)
        {:fridge (assoc-in fridge [:food name :quantity] rest)
         :picked {:quantity quantity :unit unit}}
        (no-pick :fridge fridge)))
    (no-pick :fridge fridge)))

(pick-fridge-ingredient -fridge nil :salted-butter -1 :gram)
(pick-fridge-ingredient -fridge nil :salted-butter 0 :gram)
(pick-fridge-ingredient -fridge nil :salted-butter 199 :gram)
(pick-fridge-ingredient -fridge nil :salted-butter 200 :gram)
(pick-fridge-ingredient -fridge nil :salted-butter 201 :gram)
(pick-fridge-ingredient -fridge nil :duh 5 :gram)

(def -shelf {:cans {:tomato-sauce {:quantity 1000 :unit :milliliter}}
             :spices {:salt {:quantity 100 :unit :gram}
                      :black-pepper {:quantity 100 :unit :gram}
                      :thyme {:quantity 50 :unit :sprig}
                      :garlic {:quantity 30 :unit :clove}}
             :cereals {:flour {:quantity 1000 :unit :gram}}
             :oils {:olive {:quantity 1000 :unit :milliliter}}})

(defn pick-shelf-ingredient
  [shelf type name quantity unit]
  (if-let [ingredient (when (>= quantity 0) (get-in shelf [type name]))]
    (let [ingredient-quantity (convert quantity unit (:unit ingredient))
          rest (- (:quantity ingredient) ingredient-quantity)]
      (if (>= rest 0)
        {:shelf (assoc-in shelf [type name :quantity] rest)
         :picked {:quantity quantity :unit unit}}
        (no-pick :shelf shelf)))
    (no-pick :shelf shelf)))

(pick-shelf-ingredient -shelf :spices :thyme -1 :sprig)
(pick-shelf-ingredient -shelf :spices :thyme 1 :gram)
(pick-shelf-ingredient -shelf :spices :thyme 49 :sprig)
(pick-shelf-ingredient -shelf :spices :thyme 50 :sprig)
(pick-shelf-ingredient -shelf :spices :thyme 51 :sprig)
(pick-shelf-ingredient -shelf :spices :duh 51 :sprig)
(pick-shelf-ingredient -shelf :duh :duh 51 :sprig)

(defmacro slice
  [ingredients ingredient-kw]
  (let [sliced-ingredient-kw (keyword (str "sliced-" (name ingredient-kw)))]
    `(-> ~ingredients
         (dissoc ~ingredient-kw)
         (assoc ~sliced-ingredient-kw (get ~ingredients ~ingredient-kw)))))

(macroexpand '(slice {:plop "plop"} :plop))

(defn slice-vegetables
  [ingredients]
  (-> ingredients
      (slice :potatoes)
      (slice :yellow-squashes)
      (slice :red-bell-pepper)
      (slice :eggplants)
      (slice :zucchinis)))

(slice-vegetables {:potatoes "potatoes"
                   :yellow-squashes "yellow-squashes"
                   :red-bell-pepper "red-bell-pepper"
                   :eggplants "eggplants"
                   :zucchinis "zucchinis"
                   :flour "flour"})

(defn pick-tomato-sauce! [shelf]
  (dosync
    (let [result (pick-shelf-ingredient @shelf :cans :tomato-sauce 400 :milliliter)]
      (ref-set shelf (:shelf result))
      (:picked result))))

(defn pick-garlic! [shelf]
  (dosync
    (let [result (pick-shelf-ingredient @shelf :spices :garlic 10 :gram)]
      (ref-set shelf (:shelf result))
      (:picked result))))

(defn pick-thyme! [shelf]
  (dosync
    (let [result (pick-shelf-ingredient @shelf :spices :thyme 2 :gram)]
      (ref-set shelf (:shelf result))
      (:picked result))))

(defn pick-olive-oil! [shelf]
  (dosync
    (let [result (pick-shelf-ingredient @shelf :oils :olive 3 :milliliter)]
      (ref-set shelf (:shelf result))
      (:picked result))))

(defn pick-salt! [shelf]
  (dosync
    (let [result (pick-shelf-ingredient @shelf :spices :salt 10 :gram)]
      (ref-set shelf (:shelf result))
      (:picked result))))

(defn pick-pepper! [shelf]
  (dosync
    (let [result (pick-shelf-ingredient @shelf :spices :black-pepper 5 :gram)]
      (ref-set shelf (:shelf result))
      (:picked result))))

(defn pick-flour! [shelf]
  (dosync
    (let [result (pick-shelf-ingredient @shelf :cereals :flour 20 :gram)]
      (ref-set shelf (:shelf result))
      (:picked result))))

(defn pick-eggplants! [fridge]
  (dosync
    (let [result (pick-fridge-ingredient @fridge :vegetables :eggplants 300 :gram)]
      (ref-set fridge (:fridge result))
      (:picked result))))

(defn pick-squashes! [fridge]
  (dosync
    (let [result (pick-fridge-ingredient @fridge :vegetables :yellow-squashes 200 :gram)]
      (ref-set fridge (:fridge result))
      (:picked result))))

(defn pick-zucchinis! [fridge]
  (dosync
    (let [result (pick-fridge-ingredient @fridge :vegetables :zucchinis 200 :gram)]
      (ref-set fridge (:fridge result))
      (:picked result))))

(defn pick-bell-pepper! [fridge]
  (dosync
    (let [result (pick-fridge-ingredient @fridge :vegetables :red-bell-pepper 200 :gram)]
      (ref-set fridge (:fridge result))
      (:picked result))))

(defn pick-potatoes! [fridge]
  (dosync
    (let [result (pick-fridge-ingredient @fridge :vegetables :potatoes 300 :gram)]
      (ref-set fridge (:fridge result))
      (:picked result))))

(defn pick-butter! [fridge]
  (dosync
    (let [result (pick-fridge-ingredient @fridge :default :unsalted-butter 20 :gram)]
      (ref-set fridge (:fridge result))
      (:picked result))))

(defn pick-milk! [fridge]
  (dosync
    (let [result (pick-fridge-ingredient @fridge :default :milk 200 :milliliter)]
      (ref-set fridge (:fridge result))
      (:picked result))))

(defn pick-ingredients! [shelf fridge]
  {:tomato-sauce (pick-tomato-sauce! shelf)
   :garlic (pick-garlic! shelf)
   :thyme (pick-thyme! shelf)
   :olive (pick-olive-oil! shelf)
   :salt (pick-salt! shelf)
   :black-pepper (pick-pepper! shelf)
   :flour (pick-flour! shelf)
   :eggplants (pick-eggplants! fridge)
   :yellow-squashes (pick-squashes! fridge)
   :zucchinis (pick-zucchinis! fridge)
   :red-bell-pepper (pick-bell-pepper! fridge)
   :potatoes (pick-potatoes! fridge)
   :butter (pick-butter! fridge)
   :milk (pick-milk! fridge)})

(def shelf        (ref -shelf))
(def fridge       (ref -fridge))

(pick-ingredients! shelf fridge)

@shelf
@fridge

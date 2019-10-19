# Ratatouille

> Anyone can cook, but only the fearless can be great.
> — Auguste Gusteau

![ratatouille](doc/ratatouille.png)

Let's see if you can follow this path and overcome one of the most primal fear of modern developers: parentheses.

This repository is the basis for a hands-on workshop where attendees fire up a Clojure REPL and leave it only
at the end of the day, if at all...

Structure:

* `instructor` are for you to follow along the instructor's lessons and live code "at the REPL" at the same time.
* `koans` are a famous way to learn a new language or technology by practicing it and solving small problems.
* `ratatouille` is for fun.

## Setup

Install Cursive IDE and use your own licence (you can get a free Non-commercial) or ask the instructor for
the licence he should have prepared for the event.

After installing the Cursive plugin, activate the licence and you're good to go.

### Start a REPL session

First import the project ratatouille in IntelliJ and make sure you are using JDK 8 or 11 (see "Project Structure").

Then create a new REPL configuration:

1. "Add Configuration"
2. Select the "+" sign
3. Under "Clojure REPL", select "Local"
4. Name it "REPL" and leave all other fields as default
5. Save and close
6. Run 'REPL'
7. Type `(clojure-version)` in the bottom-right pane
8. The result should show in the panel right above `=> "1.10.0"`

Well done, if Clojure sticks with you this will probably become the first
thing you do when opening a project and it is common practice amongst
Clojurians/Clojurists to almost never exit a REPL.

### Running the Koans

Leiningen is the de facto standard dependency management/building tool in
the Clojure world.

More recently, a dependency management system called "deps" has been released
by the Clojure core team, but its use is not yet ubiquitous so we'll stick
with Leiningen as it is really popular and is embedded with Cursive too.

Create a new Leiningen configuration:

1. "Add configuration"
2. Select the "+" sign
3. Select "Leiningen"
4. Name it "koans"
5. In "Arguments" type "koan run"
6. Save and close
7. Run 'koans'
8. A new "Run" tab pane should open and display an error


    Now meditate upon 00_syntax_01_equalities.clj:6
    ---------------------
    Assertion failed!
    We shall contemplate truth by testing reality, via equality.
    (= __ true)

Congratulations, we're all set up and ready to start.

The output is telling you that you have a failing test in `src/koans/00_syntax_01_equalities.clj`.
Open that file up and make it pass!  
In general, you just fill in the blanks to make tests pass.  
Sometimes there are several (or even an infinite number) of correct answers: any of them will work in these cases.

The koans differ from normal TDD in that the tests are already written for you, so you'll have to pay close attention
to the failure messages, because up until the very end, making a test pass just means that the next failure message
comes up.

While it might be easy (especially at first) to just fill in the blanks making things pass,
you should work thoughtfully, **making sure you understand why the answer is what it is**.  
Enjoy your path to enlightenment!

### Before we start

* Keep the [Clojure 1.10 Cheat Sheet](https://clojure.org/api/cheatsheet) at hand
* Use and abuse [ClojureDocs.org](https://clojuredocs.org/) to find docstrings and explore related functions
* And of course the source of truth: [clojure.org reference pages](https://clojure.org/reference/documentation)

## Credit

The `koans` are directly taken from functional-koans/clojure-koans and futurice/clojure-workshop,
with a few minor additions/subtractions here and there to suit my taste.

The instructions under `instructor` are reworded/reorganized pieces from
[Timothy Pratley's "Enterprise Clojure Training"](https://enterpriseclojure.com/)
and
[Roman Liutikov's "rc18-clojure-workshop"](https://github.com/roman01la/rc18-clojure-workshop).

All credit should go to those people for their incredible work and for releasing it under permissive licenses.  
I have only assembled the pieces and am serving as your humble instructor today.

Finally, the `ratatouille` package is a work of my own.

## Go further

Here's a list great resource to dive deeper into Clojure:

* [Clojure.org Guides](https://clojure.org/guides/getting_started)
* [Rich Hickey "Are We There Yet" talk](https://www.infoq.com/presentations/Are-We-There-Yet-Rich-Hickey/)
* [Learn Clojure](https://blog.brunobonacci.com/2016/05/11/clojure-basics/)
* [Clojure from the ground up](https://aphyr.com/tags/Clojure-from-the-ground-up)
* [Cognitect Clojure Lab (workshop)](https://github.com/cognitect/clojure-lab)
* [Carin Meier's Wonderland Clojure Katas](https://github.com/gigasquid/wonderland-clojure-katas)
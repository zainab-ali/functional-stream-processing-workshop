# Functional stream processing exercises

This repository contains code examples, demos and exercises for the [Functional Stream Processing](https://pureasync.gumroad.com/l/functional-stream-processing-in-scala) book.

## Setup instructions

Use `scala` version 3.5+.

 - Install the [scala command](https://docs.scala-lang.org/getting-started/install-scala.html)
 - Clone this repository.
 - Run:
   ```sh
   scala compile --test project.scala scala
   ```
 - Run:
   ```sh
   scala setup-ide .
   ```
 - Open the project in your favourite IDE.

### If you have problems with your IDE

Upgrade to your latest IDE version. Old versions of IntelliJ are known to have problems.

## Opening a REPL

Run:

```
scala repl project.scala
```

## Checking your solutions

Each exercise is a test. You can check your solutions by running the tests.

Run a single test, for example `Ex1WhatAreStreams.test.scala` with:

```sh
scala test project.scala scala/Part1Introduction/Ex1WhatAreStreams.test.scala
```

Run all tests with:

```sh
scala test project.scala scala
```

Some exercises have solutions in a `solutions.test.scala` file. You can consult this file if you get stuck.

## Generating the aquascapes

You can generate the aquascapes with `scala`

```sh
scala --power package \
      -o aquascape-playground/App.js \
	  -f --js project.scala scalajs/WorkshopAquascapeApp.scala scala/Part1Introduction/Fig2Take.scala
```
View your aquascape by navigating to the `aquascape-playground` directory and running a webserver of your choice:

```sh
cd aquascape-playground
althttpd
```

Alternatively, you can generate a PNG image:

```sh
scala run project.scala scala/WorkshopAquascapeApp.scala scala/Part1Introduction/Fig2Take.scala
```

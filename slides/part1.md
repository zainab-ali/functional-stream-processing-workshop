
class: center, middle

# Supercharge your streams
## Zainab Ali
## https://github.com/zainab-ali/functional-stream-processing-workshop/
### Part 1: Introduction

---
class: middle
# Agenda
 -  9:00 - 10:20 Introduction
 - 10:30 - 11:50 Handling side effects
 - 12:00 - 13:30 Concurrent systems
 - 13:30 - 15:00 🍲 Lunch
 - 15:00 - 16:00 Systems at scale
 - 16:10 - 17:30 Simulation game

🍵 10 minute breaks

???

Comfort: toilets, nametags, pen and paper.

---
class: center, middle

# What we'll do
 - Live coding
 - Coding exercises
 - Questions
 - Discussions

???

---
class: center, middle

# Hello!

## Introduce yourself

---
class: middle
# Part 1: Introduction
 - What is functional stream processing?
 - Designing incremental pipelines: frequent elements problem
 - Reasoning about stream processing: aquascape
 - Combining streams sequentially
 - Fan-out / fan-in operations
 - 🍵 10 minute break

???

You drive, ask questions. I'm here to answer them.

---
class: middle

# What is a stream?

Streams are used for:
 - data processing
 - handling events
 
???
TODO: Get the slides from fs2 pierian stream presentation

Ask people:
 - What streaming frameworks have you heard of?

---
class: middle

# Examples of stream processing frameworks

 - Reactive streams
 - Pekko streams
 - ReactiveX

---
class: middle

# fs2
 - Functional streams for Scala
 - Underpins the Typelevel ecosystem

---
class: center, middle

# Live coding
## Streams vs Lists

???

Check out the repository for the workshop.

TODO: How do we know when people are done?
How much time should we give them?

---
class: middle

# What is a stream?

 - An iterative program
 - Composed using operators
 - Has a similar API to list
 - Compiled into a value
 
---
class: middle

# Questions

 - Can you ever encounter an `OutOfMemoryError` when using `fs2`?
   - Predict the outcome of `numbersStream.toList`
   - Predict the outcome of `Stream.emits(List.range(0, Int.MaxValue))`

???

End time: 09:20

---
class: center, middle

# Designing incremental pipelines

???

Start time: 9:20

---
class: middle

# Frequent elements problem

Find the most commonly occurring element in a collection.

Examples:
 - Finding the user who interacts the most with an API.
 - Finding the most common word written by Charles Dickens.

---
class: middle

# In memory solution

```scala
def mostCommonInList(numbers: List[Int]): Int = {
  val initialCounts: Map[Int, Int] = Map.empty
  val finalCounts = numbers.foldLeft(initialCounts)((counts, i) =>
    counts + ((i, counts.getOrElse(i, 0) + 1))
  )
  finalCounts.maxBy(_._2)._1
}
```

## Problems?

---
class: middle

# Problems of in-memory approach

 - We run out of memory if `numbers` is large.
 - `numbers` cannot be infinite.

???

---
class: middle

# Stream-based approach

```scala
def mostCommon(numbers: Stream[Pure, Int]): Int = {
  val initialCounts: Map[Int, Int] = Map.empty
  val finalCounts = numbers
    .fold(initialCounts)((counts, i) =>
      counts + ((i, counts.getOrElse(i, 0) + 1))
    )
    .compile
    .last
    .getOrElse(initialCounts)
  finalCounts.maxBy(_._2)._1
}
```

## Problems?

---
class: middle

# Problems of stream-based approach

Predict the output of `mostCommon(Stream.range(0, Int.MaxValue))`

---
class: middle

# Misra Gries summary approach

 - Limit the map at a fixed size
 - Evict elements if they are not common

???

TODO: add pseudocode here

---
class: middle, center

# Exercise: incremental pipelines
## Implement Misra Gries summary

---
class: middle

# Questions
 - When should we use an in-memory datastructure vs a stream?
 - Could we use a `Stream[Pure, (Int, Int)]` instead of a `Map[Int, Int]`?
 - What tradeoffs does the incremental pipeline have?

---
class: center, middle

# Reasoning about stream processing

???

Start time: 09:35

---
class: middle

# Stream type parameters 

A stream has type `Stream[F[_], A]`

 - `F[_]` is the effect type
 - `A` is the element type

---
class: middle

# Questions
 - What does a stream of type `Stream[Pure, Nothing]` output?
 - What does a stream of type `Stream[Pure, Unit]` output?
 - Is a stream of type `Stream[Pure, String]` guaranteed to output at least one value of type `String`?

---
class: middle

# The `Nothing` type

```scala
Stream.empty: Stream[Pure, Nothing]
```

---
class: middle

# Infinite streams
 
Do these programs terminate?

```scala
Stream(1).repeat.compile.count
```

```scala
Stream.empty.repeat.compile.count
```

```scala
Stream(1).repeat
  .take(2)
  .compile
  .count
```

---
class: middle
# Questions
 - Is an infinite pure stream ever useful?
 - Is a pure stream of `Nothing` ever useful?

---
class: middle
# A model of evaluation

```scala
Stream(1)         // Stream(1) stage
  .repeat         // repeat stage
  .take(2)        // take(2) stage
  .compile.count  // compile.count stage
```
---
class: middle, center
# Live coding
## aquascapes

---
class: middle
# Key takeaways
 - The type of a stream tells us the values it outputs.
 - A stream may be finite or infinite. This is not described by its type.
 - A stream is a composable iterative program.
 - We can use the stage model to predict the behaviour of a stream.

---
class: middle

# Predict the output

```scala
Stream.empty.repeat.take(1).compile.toList
```

---
class: center, middle

# Combining streams

???

Start time: 09:50

---
class: middle

# Examples
 - Indexing elements. 
 - Adding a header and footer around a stream of paragraph text.
 - Transform a stream of book titles and into a stream of text content.
 - Fan-out and fan-in operations to parallelize tasks.

---
class: middle
# Operators

 - `append`, `++`
 - `zip`
 - `flatMap`
 - `merge`
 - `parJoin`

Distinguished by:

 - The order in which it pull elements from either stream.
 - Whether it pulls elements synchronously or concurrently.

---
class: middle, center
# Live coding
## combining operators & aquascapes

---
class: middle, center
# Exercise
## Combining

---
class: middle
# Key takeaways

- Streams can be combined synchronously using `zip`, `append` and `flatMap`.
 - Combining streams is a mechanism for composing an iterative program.

---
class: middle

# Combining streams concurrently

???

Start time: 10:05

---
class: middle

# Fan in / fan-out
 
Fan-in examples:
 - Aggregating logs from multiple processes.
 - The reduce phase of a map-reduce pipeline.

Streams are evaluated concurrently.

---
class: middle

# Pseudocode

```scala
import fs2.*
import cats.effect.IO

def fanOutAndIn[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, B] = input.map(fanFunction).parJoinUnbounded

```
---
class: middle

# Case study: word counting

 - Stream the titles of each book.
 - Given a book title, we will stream its text contents.
 - Count the number of words in the text.
 - Finally, we will sum the counts to get a total.

---
class: middle, center

# Live coding
## Test data generation

---
class: middle

# Exercise
 - Experiment with the sleep time.
 - Experiment with the number of generated sentences per book.
 
---
class: middle

# Questions
 - Is a fan-out / fan-in pipeline always faster?
 - What properties of word counting lend itself to a fan-out / fan-in architecture?
 - Should we use fan-out / fan-in for frequent elements?

---
class: middle

# Key takeaways
 - A fan-out is encoded as a `Stream[IO, Stream[IO, A]]`
 - Streams are evaluated concurrently.
 - Lead to performance improvements if problem is mostly waiting.
 - Not great for compute-intensive tasks.

---
class: middle, center

# Coffee break

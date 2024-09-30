
class: center, middle

# Supercharge your streams
## Zainab Ali
### Part 2: Handling side effects

???

Start time: 10:30

---
class: middle
# Part 2: Handling side effects
 - Working with cats-effect IO
 - Working with cats-effect IO and streams
 - Error propagation in streams, recovery and retries
 - Resources and bracketing
 - Performance optimization: chunking
 - 🍵 10 minute break

???

TODO: Have a recap before this.

---
class: middle, center

# What is a side-effect?
---
class: middle

# A side-effecting expression
 - Does something other than the evaluate to a value
 - Cannot be replaced with its value
 - `println("Hello!")` returns `Unit`
 - `println("Hello!")` is not equivalent to `()`
---
class: center, middle
# Live coding
## `CatsEffectIO.sc`

---
class: middle
# cats-effect IO
 - Used to suspend side-effecting expressions
 - Side-effects aren't evaluated
 - Expressions can be composed
 - `unsafeRunSync()` used to run in consolse
 - `IOApp` used in production code

---
class: center, middle
# Effectful streams

???

Start time: 10:45

---
class: center, middle

# Live coding
## `EffectfulStreams.sc`
## `Fig1Effects.scala`

???
Start time: 10:45

---
class: middle

# Key takeaways

 - Operators for side-effects are prefixed by `eval`.
 - Streams might not output elements.
 - Streams compile into an `IO`.
 - A side-effecting `Stream` has type `Stream[IO, A]`.
---
class: middle

# Questions

 - Is a `Stream[IO, Nothing]` ever useful?
 - Will a `Stream[IO, Nothing]` ever terminate?
 - When would you use `eval` vs `exec`?
 - When would you use `evalMap` vs `evalTap`?

---
class: center, middle

# Exercise
## `Ex1Effects.test.scala`

---
class: center, middle

# Live coding
## `FilesApi.sc`

---
class: center, middle

# Exercise
## `Ex2WordCount.scala`

---
class: center, middle
# Error propagation, recovery and retries

???

Start time: 11:00

---
class: center, middle
# What is an error?
---
class: middle
# An error is
 - An exceptional case
 - Requires different control flow
## Examples
 - Read a file that doesn't exist.
 - Calling an API that is down.
 - DB connection pool is starved.

---
class: center, middle
# Live coding
## `Errors.sc`
## `HandleErrorWith.scala`

---
class: middle

# Questions

 - If an error is raised in a stream, can we continue execution?
 - Why?
 - When should we retry on error?

---
class: middle

# Retries

Using recursion

```scala
def numbers: Stream[IO, Int] = Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .handleErrorWith(_ => numbers)
```

???

---
class: middle
# Key takeaways

 - A side effecting stream may terminate with an error.
 - An error is a control flow construct used to terminate a stream.
 - It is not possible to recover a stream after an error
 - `handleError` variants construct new streams.

---
class: center, middle

# Exercise
## `Errors.test.scala`

---
class: middle
# Exercise

1. Change the permissions of `data/little-dorrit.txt` to deny reading:

```sh
chmod u-x data/little-dorrit.txt
```

2. Run the `WordCount` app.
3. Modify the code to ignore books that cannot be read.

---
class: center, middle

# Bracketing with streams

???

Start time: 11:15

---
class: center, middle

# When should we use a bracket? 

---
class: middle

# We should use a bracket when 
 - Working with resources.
  - File handles must be returned.
  - Connections must be returned to a pool.
 - Executing code on error and cancellation.
  - Logging.

---
class: center, middle

# Live coding

---
class: middle
## Key takeaways

 - Bracketing is used to describe cleanup code.
 - Resources will be released even if a program fails.
 - Resources can be acquired and released over the lifetime of a stream.

---
class: center, middle

# Exercise
## `Bracket.test.scala`

---
class: middle
# Performance optimization with chunks

 - Elements are pulled in batches, called chunks.
 - A chunk is similar to a list, but optimized.

???

Start time: 11:30


---
class: center, middle

# Live coding
## `Chunks.sc`

???

End with aquascapes

---
class: middle

# Picking a chunk size

 - Has performance benefits.
 - Specified at source e.g `readUtf8`
 - Too large chunks cause memory issues.
 - Too small chunks also cause memory issues.

---
class: middle

# Key takeaways

 - Operators pull elements in chunks.
 - Most operators preserve chunks.
 - `eval` operators do not preserve chunks
 - `evalChunk` operators preserve chunks, but have different behaviour.
 - You should specify a chunk size when constructing a stream.

---
class: center, middle
# Coffee break

???

We have a 15 minute grace.

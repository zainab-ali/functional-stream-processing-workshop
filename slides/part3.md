
class: center, middle

# Supercharge your streams
## Zainab Ali
### Part 3: Concurrent systems

---
class: middle
# Part 3: Concurrent systems
 - Concurrency and parallelism
 - Managing concurrent state
 - 🍵 Lunch break

???

TODO: Have a recap before this.

Start time: 12:00

---
class: center, middle
# What is concurrency?
---
class: middle
# What is concurrency?
 - Evaluating tasks "at the same time"
 - Performing tasks independently of each other
---
class: middle
# Concurrency is challenging

 - Non-determinism.
 - Complex error handling.
 - More elements processed than expected.

---
class: center, middle
# Live coding
## `Concurrency.sc`

---
class: middle
# Cancelation
 - In-progress side effects are canceled.
 - A canceled `IO` may not fully complete.
 - Code should be designed to be cancelation safe.
 - `ExitCase.Canceled` observed in `onFinalizeCase`.
---

class: middle
# Concurrent operators
 - Operators generally have a `par` prefix
 - Marked with the `Concurrent` typeclass

---
class: center, middle

# Managing concurrent state

???

Start time: 12:45

---
class: center, middle
# Live coding
## `ConcurrentState.sc`

---
class: center, middle
# Exercise
## `Metrics.test.scala`

---
class: middle
# Key takeaways
 - `Ref` is used to store shared state.
 - `SignallingRef` is used to stream state changes.
 - `discrete` constructs a blocking stream
 - `continuous` is non-blocking

---
class: center, middle

# Lunch break

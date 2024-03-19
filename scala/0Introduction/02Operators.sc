// Run this in the repl with:
// scala-cli repl project.scala

import fs2.*

/* fs2 provides us with many operators for working with streams. We can easily compose them. */
val numbers = Stream.range(0, Int.MaxValue)

/* The take and drop operators are analogous to the list take and drop. repeat "repeats" a stream. */
val ascii = numbers.drop(70).take(4).repeat.take(7).map(_.toChar).compile.toList

// Run this in the repl with:
// scala-cli repl --dep "co.fs2::fs2-core:3.9.4" --scala 3.4.0

import fs2.*

val numbers = Stream.range(0, Int.MaxValue)

val zipped = numbers.zip(numbers.map(_ + 1)).take(3).compile.toList
val appended = (numbers.take(3) ++ numbers.take(4)).compile.toList

val merged = numbers.merge(numbers.map(_ + 1)).take(3).compile.toList

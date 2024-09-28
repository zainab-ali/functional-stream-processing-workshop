// Run this in the repl with:
// scala-cli repl --dep "co.fs2::fs2-core:3.9.4" --scala 3.4.0

import fs2.*

val helloWords = Stream("Hello", "Cádiz")
val goodbyeWords = Stream("Goodbye", "London")

(helloWords ++ goodbyeWords).compile.toList

helloWords.zip(goodbyeWords).compile.toList

helloWords.interleave(goodbyeWords).compile.toList
helloWords
  .flatMap(helloWord =>
    goodbyeWords.map(goodbyeWord => s"$helloWord-$goodbyeWord")
  )
  .compile
  .toList

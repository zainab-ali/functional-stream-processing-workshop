import fs2.*

/* Streams vs Lists */

/* fs2 stream operators are similar to those of lists. For example, there is a range operator. However there is a huge difference between streams and lists. To explore this, consider the stream of numbers from 0 to Int.MaxValue: */
val numbersList = List.range(0, Int.MaxValue)
val numbers = Stream.range(0, Int.MaxValue)

val numbersListCount = numbersList.count

/* A stream is a program that is "compiled" into a value. For example, we can compile it into the count of elements. */
val numbersCount = numbers.compile.count

/* We can create a stream backed by a list with the emits function  */
val numbersFromList = Stream.emits(List.range(0, Int.MaxValue))

/* We can compile a stream to a list with the toList function */
val numbersListFromStream = numbers.toList

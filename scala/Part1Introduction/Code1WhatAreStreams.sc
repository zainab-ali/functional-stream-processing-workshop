import fs2.*

/* Functional stream processing */

/* A list-based approach is testable and easy to reason through, but runs out of memory for large values */
def countNumbersList(until: Int, predicate: Int => Boolean): Int =
  List.range(0, until).filter(predicate).size

/* A loop is not composable, so is not easy to test or reason through */
def countNumbersLoop(until: Int, predicate: Int => Boolean): Int = {
  var count: Int = 0
  var i: Int = 0
  while (i < until) {
    if (predicate(i)) count = count + 1
    i = i + 1
  }
  count
}

/* Explicit recursion is also not composable */
def countNumbersRec(until: Int, predicate: Int => Boolean): Int = {
  @scala.annotation.tailrec
  def go(count: Int, i: Int): Int = {
    if (i < until) {
      if (predicate(i)) {
        go(count + 1, i + 1)
      } else {
        go(count, i + 1)
      }
    } else {
      count
    }
  }
  go(0, 0)
}

/* A stream is composable, testable and can be reasoned through */
def countNumbersStream(until: Int, predicate: Int => Boolean): Long =
  Stream.range(0, until).filter(predicate).compile.count.toInt

/* Streams vs Lists */

/* fs2 stream operators are similar to those of lists. For example, there is a range operator. However there is a huge difference between streams and lists. To explore this, consider the stream of numbers from 0 to Int.MaxValue: */
val numbersList = List.range(0, Int.MaxValue)
val numbers = Stream.range(0, Int.MaxValue)

val numbersListCount = numbersList.size

/* A stream is a program that is "compiled" into a value. For example, we can compile it into the count of elements. */
val numbersCount = numbers.compile.count

/* We can create a stream backed by a list with the emits function  */
val numbersFromList = Stream.emits(List.range(0, Int.MaxValue))

/* We can compile a stream to a list with the toList function */
val numbersListFromStream = numbers.toList

import munit.*
import fs2.*

/** Aim: Find the most commonly occurring element in a list of elements. */
class Ex0IncrementalPipelines extends CatsEffectSuite {

  val numbers: Stream[Pure, Int] = Stream.range(0, Int.MaxValue)

  val oneAndFortyTwo: Stream[Pure, Int] = Stream(42, 1, 42).repeatN(100)

  def mostCommon(k: Int, numbers: Stream[Pure, Int]): Int = {
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

  test("The answer is forty two") {
    assertEquals(mostCommon(k = 100, oneAndFortyTwo), 42)
  }

  test("The most common element is present") {
    assert(clue(mostCommon(k = 100, numbers.tail)) != 0)
  }

  test("the most common element among mostly forty twos is forty two") {
    // Construct your own test stream
    val testData: Stream[Pure, Int] = ???
    assertEquals(testData.take(6).toList, List(0, 42, 1, 42, 2, 42))
    assertEquals(mostCommon(k = 100, testData), 42)
  }
}

import munit.*
import fs2.*

class Ex2IncrementalPipelinesSolutions extends CatsEffectSuite {

  val userIds: Stream[Pure, String] =
    Stream
      .range(0, Int.MaxValue)
      .map(id => s"u$id")
      .take(
        100
      ) // This `take(100)` speeds up test execution in CI. It is not needed for passing the test.

  val oneAndFortyTwo: Stream[Pure, String] =
    Stream("u42", "u1", "u42").repeatN(100)

  def mostCommon(k: Int, ids: Stream[Pure, String]): String = {
    // Replace this implementation with the Misra Gries summary.
    val initialCounts: Map[String, Int] = Map.empty
    val finalCounts = ids
      .fold(initialCounts) { (counts, id) =>
        counts.get(id) match {
          case Some(previousCount)       => counts + ((id, previousCount + 1))
          case None if (counts.size < k) => counts + ((id, 1))
          case None =>
            counts.collect {
              case (id, count) if count > 1 => (id, count - 1)
            }
        }
      }
      .compile
      .last
      .getOrElse(initialCounts)
    val (mostCommonId, _) = finalCounts.maxBy { case (_, count) => count }
    mostCommonId
  }

  test("The answer is forty two") {
    assertEquals(mostCommon(k = 100, oneAndFortyTwo), "u42")
  }

  test("The mostCommon function can process a large volume of ids") {
    val mostlyFortyTwo = Stream("u42").repeatN(100) ++ userIds
    assert(clue(mostCommon(k = 100, mostlyFortyTwo)) == "u42")
  }

  test("the most common element among mostly forty twos is forty two") {
    val testData: Stream[Pure, String] =
      userIds.interleave(Stream("u42").repeat).take(Int.MaxValue)
    assertEquals(mostCommon(k = 100, testData), "u42")
  }
}

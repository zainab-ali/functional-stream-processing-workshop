import fs2.*

def mostCommonInList(ids: List[String]): String = {
  val initialCounts: Map[String, Int] = Map.empty
  val finalCounts = ids.foldLeft(initialCounts) { (counts, id) =>
    val previousCount = counts.getOrElse(id, 0)
    counts + ((id, previousCount + 1))
  }
  val (mostCommonId, _) = finalCounts.maxBy { case (_, count) => count }
  mostCommonId
}

def mostCommonInStream(ids: Stream[Pure, String]): String = {
  val initialCounts: Map[String, Int] = Map.empty
  val finalCounts = ids
    .fold(initialCounts) { (counts, id) =>
      val previousCount = counts.getOrElse(id, 0)
      counts + ((id, previousCount + 1))
    }
    .compile
    .last
    .getOrElse(initialCounts)
  val (mostCommonId, _) = finalCounts.maxBy { case (_, count) => count }
  mostCommonId
}

val oneAndFortyTwo: Stream[Pure, String] =
  Stream("user42", "user1", "user42").repeatN(Int.MaxValue / 100)

mostCommonInStream(oneAndFortyTwo)

val userIds: Stream[Pure, String] =
  Stream.range(0, Int.MaxValue).map(i => s"u$i").take(Int.MaxValue / 100)

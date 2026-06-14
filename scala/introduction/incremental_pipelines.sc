def mostCommonInList(ids: List[String]): String = {
  val initialCounts: Map[String, Int] = Map.empty
  val finalCounts = ids.foldLeft(initialCounts) { (counts, id) =>
    val previousCount = counts.getOrElse(id, 0)
    counts + ((id, previousCount + 1))
  }
  val (mostCommonId, _) = finalCounts.maxBy { case (_, count) => count }
  mostCommonId
}

import fs2.*

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

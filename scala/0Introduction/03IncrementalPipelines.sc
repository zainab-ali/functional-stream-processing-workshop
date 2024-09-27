import fs2.*

def mostCommonInList(numbers: List[Int]): Int = {
  val initialCounts: Map[Int, Int] = Map.empty
  val finalCounts = numbers.foldLeft(initialCounts)((counts, i) =>
    counts + ((i, counts.getOrElse(i, 0) + 1))
  )
  finalCounts.maxBy(_._2)._1
}

def mostCommon(numbers: Stream[Pure, Int]): Int = {
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


val numbers: Stream[Pure, Int] = Stream.range(0, Int.MaxValue).take(Int.MaxValue / 100)

val oneAndFortyTwo: Stream[Pure, Int] = Stream(42, 1, 42).repeatN(Int.MaxValue / 100)

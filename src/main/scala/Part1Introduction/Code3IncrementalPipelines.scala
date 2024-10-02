package Part1Introduction


final class Code3IncrementalPipelines$_ {
def args = Code3IncrementalPipelines_sc.args$
def scriptPath = """Part1Introduction/Code3IncrementalPipelines.sc"""
/*<script>*/
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

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code3IncrementalPipelines_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code3IncrementalPipelines$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code3IncrementalPipelines_sc.script as `Code3IncrementalPipelines`


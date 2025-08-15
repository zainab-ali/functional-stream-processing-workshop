

final class Code2IncrementalPipelines$_ {
def args = Code2IncrementalPipelines_sc.args$
def scriptPath = """Code2IncrementalPipelines.sc"""
/*<script>*/
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

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code2IncrementalPipelines_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code2IncrementalPipelines$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code2IncrementalPipelines_sc.script as `Code2IncrementalPipelines`


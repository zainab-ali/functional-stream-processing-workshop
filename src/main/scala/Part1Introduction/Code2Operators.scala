package Part1Introduction


final class Code2Operators$_ {
def args = Code2Operators_sc.args$
def scriptPath = """Part1Introduction/Code2Operators.sc"""
/*<script>*/
import fs2.*

/* fs2 provides us with many operators for working with streams. We can easily compose them. */
val numbers = Stream.range(0, Int.MaxValue)

/* The take and drop operators are analogous to the list take and drop. repeat "repeats" a stream. */
val ascii = numbers.drop(70).take(4).repeat.take(7).map(_.toChar).compile.toList

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code2Operators_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code2Operators$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code2Operators_sc.script as `Code2Operators`


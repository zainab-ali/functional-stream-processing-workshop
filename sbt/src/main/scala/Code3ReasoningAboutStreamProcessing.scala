

final class Code3ReasoningAboutStreamProcessing$_ {
def args = Code3ReasoningAboutStreamProcessing_sc.args$
def scriptPath = """Code3ReasoningAboutStreamProcessing.sc"""
/*<script>*/
import fs2.*

/** The following stream outputs no values */
Stream.empty

/** This stream is infinite */
val ones = Stream(1).repeat

/** What happens when it is compiled and run? */
ones.compile.count

/** An infinite empty stream contains no values */
val empties = Stream.empty.repeat

/** What happens when it is compiled and run? */
empties.compile.count

/** A finite stream can be constructed from an infinite stream by composing
  * operators.
  */
Stream(1).repeat
  .take(2)
  .compile
  .count

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code3ReasoningAboutStreamProcessing_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code3ReasoningAboutStreamProcessing$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code3ReasoningAboutStreamProcessing_sc.script as `Code3ReasoningAboutStreamProcessing`


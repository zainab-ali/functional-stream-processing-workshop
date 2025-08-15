

final class Code4CombiningOperators$_ {
def args = Code4CombiningOperators_sc.args$
def scriptPath = """Code4CombiningOperators.sc"""
/*<script>*/
import fs2.*

val helloWords = Stream("Hello", "Cádiz")
val goodbyeWords = Stream("Goodbye", "London")

/** ++ is an alias for append */
(helloWords ++ goodbyeWords).compile.toList

helloWords.zip(goodbyeWords).compile.toList

helloWords.interleave(goodbyeWords).compile.toList
helloWords
  .flatMap(helloWord =>
    goodbyeWords.map(goodbyeWord => s"$helloWord-$goodbyeWord")
  )
  .compile
  .toList

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code4CombiningOperators_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code4CombiningOperators$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code4CombiningOperators_sc.script as `Code4CombiningOperators`


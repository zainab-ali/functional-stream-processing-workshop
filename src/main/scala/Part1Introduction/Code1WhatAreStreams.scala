package Part1Introduction


final class Code1WhatAreStreams$_ {
def args = Code1WhatAreStreams_sc.args$
def scriptPath = """Part1Introduction/Code1WhatAreStreams.sc"""
/*<script>*/
import fs2.*

/* Streams vs Lists */

/* fs2 stream operators are similar to those of lists. For example, there is a range operator. However there is a huge difference between streams and lists. To explore this, consider the stream of numbers from 0 to Int.MaxValue: */
val numbersList = List.range(0, Int.MaxValue)
val numbers = Stream.range(0, Int.MaxValue)

val numbersListCount = numbersList.count

/* A stream is a program that is "compiled" into a value. For example, we can compile it into the count of elements. */
val numbersCount = numbers.compile.count

/* We can create a stream backed by a list with the emits function  */
val numbersFromList = Stream.emits(List.range(0, Int.MaxValue))

/* We can compile a stream to a list with the toList function */
val numbersListFromStream = numbers.toList

// Evaluation
// Some trivial streams
// val emptyStream = Stream.empty.repeat
// println(s"This never terminates: ${emptyStream.compile.count}")

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code1WhatAreStreams_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code1WhatAreStreams$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code1WhatAreStreams_sc.script as `Code1WhatAreStreams`


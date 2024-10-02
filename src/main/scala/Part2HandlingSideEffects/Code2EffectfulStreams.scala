package Part2HandlingSideEffects


final class Code2EffectfulStreams$_ {
def args = Code2EffectfulStreams_sc.args$
def scriptPath = """Part2HandlingSideEffects/Code2EffectfulStreams.sc"""
/*<script>*/
import cats.effect.*
import cats.effect.unsafe.implicits.global

val greetingIO = IO.println("Hello!")

import fs2.*
val greetingStream = Stream.eval(greetingIO)

Stream.exec(greetingIO)

Stream(1, 2, 3)
  .evalMap(n => IO.println(s"The number is $n."))
  .compile
  .count
  .unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code2EffectfulStreams_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code2EffectfulStreams$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code2EffectfulStreams_sc.script as `Code2EffectfulStreams`




final class Code1CatsEffectIO$_ {
def args = Code1CatsEffectIO_sc.args$
def scriptPath = """Code1CatsEffectIO.sc"""
/*<script>*/
val greeting: Unit = println("Hello!")

import cats.effect.IO

val greetingIO0 = IO(println("Hello!"))

val greetingIO = IO.println("Hello!")

val greetTwice =
  greetingIO.flatMap(_ => greetingIO)

import cats.effect.unsafe.implicits.global
greetTwice.unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code1CatsEffectIO_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code1CatsEffectIO$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code1CatsEffectIO_sc.script as `Code1CatsEffectIO`


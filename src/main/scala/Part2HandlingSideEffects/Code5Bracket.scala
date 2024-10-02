package Part2HandlingSideEffects


final class Code5Bracket$_ {
def args = Code5Bracket_sc.args$
def scriptPath = """Part2HandlingSideEffects/Code5Bracket.sc"""
/*<script>*/
import fs2.*
import cats.effect.IO
import cats.effect.unsafe.implicits.*

Stream
  .bracket(IO.println("Acquiring"))(_ => IO.println("Releasing"))
  .compile
  .drain
  .unsafeRunSync()

Stream
  .bracket(IO.println("Acquiring").as(1))(n => IO.println(s"Releasing $n"))
  .flatMap { n =>
    Stream.eval(IO.println(s"Using $n"))
  }
  .compile
  .drain
  .unsafeRunSync()

Stream
  .bracket(IO.println("Acquiring").as(1))(n => IO.println(s"Releasing $n"))
  .flatMap { n =>
    Stream.eval(IO.println(s"Using $n"))
  }
  .repeatN(3)
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .take(2)
  .debug()
  .onFinalize(IO.println("Finished"))
  .compile
  .toList
  .unsafeRunSync()

Stream(1, 2, 3)
  .take(2)
  .debug()
  .onFinalizeCase(exitCase => IO.println(s"Finished with $exitCase"))
  .compile
  .toList
  .unsafeRunSync()

Stream
  .raiseError[IO](new Error("Boom!"))
  .onFinalizeCase(exitCase => IO.println(s"Finished with $exitCase"))
  .compile
  .toList
  .unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code5Bracket_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code5Bracket$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code5Bracket_sc.script as `Code5Bracket`


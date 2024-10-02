package Part2HandlingSideEffects


final class Code4Errors$_ {
def args = Code4Errors_sc.args$
def scriptPath = """Part2HandlingSideEffects/Code4Errors.sc"""
/*<script>*/
import fs2.*
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import scala.util.control.NoStackTrace
import cats.syntax.all.*

object BOOM extends Throwable("BOOM!") with NoStackTrace
val errStream = Stream.raiseError[IO](BOOM)

errStream.compile.drain.unsafeRunSync()

IO.raiseError(BOOM).unsafeRunSync()

def raiseIfTwo(n: Int): IO[Unit] =
  IO.raiseWhen(n == 2)(BOOM)

Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .evalMap(n => IO.println(n))
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .handleError(_ => 42)
  .compile
  .toList
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .handleErrorWith(_ => Stream(42, 43))
  .compile
  .toList
  .unsafeRunSync()

// Retries

def numbers: Stream[IO, Int] = Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .handleErrorWith(_ => numbers)

numbers
  .evalMap(x => IO.println(x))
  .take(2)
  .compile
  .drain
  .unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code4Errors_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code4Errors$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code4Errors_sc.script as `Code4Errors`




final class Code3Errors$_ {
def args = Code3Errors_sc.args$
def scriptPath = """Code3Errors.sc"""
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
  .compile
  .count
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

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code3Errors_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code3Errors$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code3Errors_sc.script as `Code3Errors`


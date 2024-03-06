import fs2.*
import cats.effect.{Trace => _, *}

/* An error is a control flow construct used to terminate a stream. */
object Example20 extends IOApp.Simple {
  def error: IO[Int] = IO.raiseError(new Error("!"))

  def run = {
    Stream.eval(error).compile.drain
  }
}

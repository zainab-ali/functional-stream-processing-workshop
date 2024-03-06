import fs2.*
import cats.effect.{Trace => _, *}

/* We can evaluate side-effects as part of a stream by using cats-effect's IO */
object Example10 extends IOApp.Simple {

  /* This side-effect prints a number to the console and returns the number */
  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def run: IO[Unit] = {
    Stream.eval(print(1)).compile.drain
  }
}

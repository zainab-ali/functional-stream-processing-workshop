import fs2.*
import cats.effect.{Trace => _, *}

/* We can evaluate side-effects as part of a stream by using evalMap and similar operators */
object Example11 extends IOApp.Simple {

  /* This side-effect prints a number to the console and returns the number */
  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def run: IO[Unit] = {
    Stream(1, 2, 3).evalMap(print).compile.drain
  }
}

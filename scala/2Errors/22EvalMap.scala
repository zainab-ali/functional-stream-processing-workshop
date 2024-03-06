import fs2.*
import cats.effect.{Trace => _, *}
import cats.syntax.all.*

/* Raising an error terminates a stream */
object Example22 extends IOApp.Simple {

  /* Raise an error when the number is 2, otherwise return the number */
  def error(x: Int): IO[Int] = IO.raiseError(new Error("!")).whenA(x == 2).as(x)

  def run = {
    Stream(1, 2, 3)
      .evalMap(error)
      /* Note that the numbers 2 and 3 are never printed */
      .evalMap(x => IO.println(x))
      .compile
      .drain
  }
}

import fs2.*
import cats.effect.{Trace => _, *}
import cats.syntax.all.*

/* The handleError / handleError operators are used to handle the error. */
object Example24 extends IOApp.Simple {

  /* Raise an error when the number is 2, otherwise return the number */
  def error(x: Int): IO[Int] = IO.raiseError(new Error("!")).whenA(x == 2).as(x)

  def run = {
    Stream(1, 2, 3)
      .evalMap(error)
      .handleError(_ => 42)
      /* Note that the numbers 2 and 3 are still not printed. The program terminates after 42 is printed. */
      .evalMap(x => IO.println(x))
      .compile
      .drain
  }
}

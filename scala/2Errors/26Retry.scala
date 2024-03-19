import fs2.*
import cats.effect.{Trace => _, *}
import cats.syntax.all.*

/* We can "retry" a stream by recursing within the handleErrorWith operator. This should be used with caution. */
object Example26 extends IOApp.Simple {

  /* Raise an error when the number is 2, otherwise return the number */
  def error(x: Int): IO[Int] = IO.raiseError(new Error("!")).whenA(x == 2).as(x)

  def run = {
    def numbers: Stream[IO, Int] = Stream(1, 2, 3)
      .evalMap(error)
      .handleErrorWith(_ => numbers)

    numbers
      .evalMap(x => IO.println(x))
      .take(2)
      .compile
      .drain
  }
}

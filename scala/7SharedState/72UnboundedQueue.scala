import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

/* A queue is a buffer of elements. It can be bounded or unbounded. */
object Example72 extends IOApp.Simple {

  def print(value: Int): IO[Unit] = IO.println(s"The value is $value")

  def run: IO[Unit] = {
    for {
      queue <- Queue.unbounded[IO, Int]
      _ <- Stream
        .awakeEvery[IO](1.second)
        .zipWithIndex
        .map(_._2.toInt)
        .evalTap(print)
        .take(4)
        .enqueueUnterminated(queue)
        .compile
        .drain
      res <- queue.take
      _ <- print(res)
    } yield ()
  }
}

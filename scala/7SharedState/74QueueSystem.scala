import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

/* We can use queues to buffer between streams.*/
object Example74 extends IOApp.Simple {

  def print(value: Int): IO[Unit] = IO.println(s"The value is $value")

  def run: IO[Unit] = {
    for {
      queue <- Queue.bounded[IO, Int](3)
      _ <- Stream
        .awakeEvery[IO](1.second)
        .zipWithIndex
        .map(_._2.toInt)
        .evalTap(print)
        .take(4)
        .enqueueUnterminated(queue)
        .merge(Stream.fromQueueUnterminated(queue).evalTap(print))
        .compile
        .drain
      res <- queue.take
      _ <- print(res)
    } yield ()
  }
}

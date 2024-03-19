import fs2.*
import fs2.concurrent.*
import cats.effect.*
import cats.effect.std.Queue
import scala.concurrent.duration.*

object Example82 extends IOApp.Simple {

  def producer(q: Queue[IO, String]) =
    Stream
      .iterate(0)(_ + 1)
      .evalMap(i => IO.println(s"Offering $i") >> q.offer(s"Item-$i"))
      .metered(1.second)

  def consumer(q: Queue[IO, String]) = Stream
    .fromQueueUnterminated(q, limit = 1)
    .evalMap(text => IO.println(s"Processing $text"))
    .metered(2.seconds)

  def run: IO[Unit] = {
    Queue.bounded[IO, String](3).flatMap { queue =>
      producer(queue).merge(consumer(queue)).compile.drain
    }
  }
}

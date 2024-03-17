import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

object Example86 extends IOApp.Simple {

  def producer(topic: Topic[IO, String]) =
    Stream
      .iterate(0)(_ + 1)
      .evalMap(i => IO.println(s"Offering $i") >> topic.publish1(s"Item-$i"))
      .metered(1.second)

  def consumer(topic: Topic[IO, String], chanId: Int, time: FiniteDuration) =
    topic.subscribe(0)
    .evalMap(text => IO.println(s"${chanId}: Processing $text"))
    .meteredStartImmediately(time)

  def run: IO[Unit] = {
    Topic[IO, String].flatMap { topic =>
      producer(topic)
      .merge(
      consumer(topic, 1, 1.second)
        .merge(consumer(topic, 2, 10.second)))
        .compile.drain
    }
  }
}

import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

object Example85 extends IOApp.Simple {

  def producer(first: Channel[IO, String], second: Channel[IO, String]) =
    Stream
      .iterate(0)(_ + 1)
      .evalMap(i => IO.println(s"Offering $i") >> first.send(s"Item-$i") >> second.send(s"Item-$i"))
      .metered(1.second)

  def consumer(chan: Channel[IO, String], chanId: Int) =
    chan.stream
    .evalMap(text => IO.println(s"${chanId}: Processing $text"))
    .metered(2.seconds)

  def run: IO[Unit] = {
    for {
      first <- Channel.bounded[IO, String](1)
      second <- Channel.bounded[IO, String](1)
      _ <- producer(first, second)
      .merge(
        consumer(first, 1)
      ).merge(consumer(second, 2))
      .compile.drain
    } yield ()
  }
}

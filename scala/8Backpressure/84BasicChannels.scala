import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

object Example84 extends IOApp.Simple {

  def producer(chan: Channel[IO, String]) =
    Stream
      .iterate(0)(_ + 1)
      .evalMap(i => IO.println(s"Offering $i") >> chan.send(s"Item-$i"))
      .metered(1.second)

  def consumer(chan: Channel[IO, String]) =
    chan.stream
      .evalMap(text => IO.println(s"Processing $text"))
      .metered(2.seconds)

  def run: IO[Unit] = {
    Channel.bounded[IO, String](1).flatMap { channel =>
      producer(channel).merge(consumer(channel)).compile.drain
    }
  }
}

import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

object Example81 extends IOApp.Simple {

  def slowEcho(text: String): IO[Unit] =
    IO.sleep(5.seconds) >> IO.println(s"Did you say $text?")

  def run: IO[Unit] = {
    Queue.bounded[IO, String](5).flatMap { queue =>
      fs2.io
        .stdinUtf8[IO](10)
        .evalMap { text =>
          IO.println(s"Offering $text") >> queue.offer(text)
        }
        .merge(Stream.fromQueueUnterminated(queue).evalMap(slowEcho))
        .compile
        .drain
    }

  }
}

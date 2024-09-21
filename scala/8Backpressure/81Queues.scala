import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

object Example81 extends IOApp.Simple {

  def slowEcho(text: String): IO[Unit] =
    IO.sleep(5.seconds) >> IO.println(s"Did you say $text?")

  def run: IO[Unit] = {
    Queue.bounded[IO, String](3).flatMap { queue =>
      fs2.io
        .stdin[IO](1024)
        .split(_ == '\n'.toByte)
        .map(chk => String(chk.toArray))
        .evalMap { text =>
          IO.println(s"Offering $text") >> queue.offer(text)
        }
        .concurrently(Stream.fromQueueUnterminated(queue).evalMap(slowEcho))
        .compile
        .drain
    }

  }
}

import fs2.*
import fs2.concurrent.*
import cats.effect.{Trace => _, *}
import cats.effect.std.Queue
import scala.concurrent.duration.*

object Example80 extends IOApp.Simple {

  def slowEcho(text: String): IO[Unit] =
    IO.sleep(5.seconds) >> IO.println(s"Did you say $text?")

  def run: IO[Unit] = {
    fs2.io.stdinUtf8[IO](10).foreach(slowEcho).compile.drain
  }
}

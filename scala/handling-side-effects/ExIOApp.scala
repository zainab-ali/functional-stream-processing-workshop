import fs2.*
import cats.effect.{Trace => _, *}
import fs2.io.file.Path

object MyApp extends IOApp.Simple {

  def run: IO[Unit] = IO(println("Hello!"))
}

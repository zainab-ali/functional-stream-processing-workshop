import fs2.*
import cats.effect.{Trace => _, *}

/* We can also use exec to evaluate an effect but not propagate a unit value. */
object Example12 extends IOApp.Simple {

  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def run: IO[Unit] = {
    Stream.exec(print(1).void).compile.drain
  }
}

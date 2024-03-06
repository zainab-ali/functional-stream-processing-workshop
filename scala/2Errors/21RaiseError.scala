import fs2.*
import cats.effect.{Trace => _, *}

/* We can also raise an error with the raiseError function */
object Example21 extends IOApp.Simple {

  def run = {
    Stream.raiseError[IO](new Error("!")).compile.drain
  }
}

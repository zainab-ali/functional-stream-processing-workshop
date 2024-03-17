import cats.effect.*
import fs2.*
import cats.effect.*
import doodle.java2d.*
import fs2.concurrent.*
import scala.concurrent.duration.*
import cats.syntax.all.*
import doodle.interact.*
import doodle.interact.syntax.all.*
import doodle.core.*

object Example92 extends IOApp.Simple {
  val game = new Game[Int, Unit] {
    def init: Int = 1
    def render(state: Int): Picture[Unit] =
      Picture.circle(state)

    def input(text: String): Option[Unit] = None

    def action(command: Unit, state: Ref[IO, Int]): Stream[IO, Nothing] =
      Stream.empty
    def simulation(state: Ref[IO, Int]): Stream[IO, Nothing] =
      Stream
        .repeatEval(state.update(_ + 1))
        .metered(10.millis)
        .drain
  }

  def run: IO[Unit] = game.run
}

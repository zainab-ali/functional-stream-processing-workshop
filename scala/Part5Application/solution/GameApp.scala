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

trait GameApp[S, C] extends IOApp.Simple {

  def game: IO[Game[S, C]]

  def run: IO[Unit] = game.flatMap { game =>
    val frame = Frame.default.withSize(600, 600).withBackground(Color.paleGreen)
    SignallingRef.of[IO, S](game.init).flatMap { stateSignal =>
      val renderLoop = stateSignal.continuous
        .map(game.render)
        .metered(20.millis)

      val actions = fs2.io
        .stdinUtf8[IO](1024)
        .map(_.trim)
        .mapFilter(game.input)
        .map(game.action(_, stateSignal))
        .parJoinUnbounded
      renderLoop
        .concurrently(actions)
        .concurrently(game.simulation(stateSignal))
        .animateToIO(frame)
    }
  }
}

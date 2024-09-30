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

trait GameAppWithError[S, C] extends IOApp.Simple {

  def game: IO[Game[S, C]]

  def run: IO[Unit] = game.flatMap { game =>
    import game.*
    val frame = Frame.default.withSize(600, 600).withBackground(Color.paleGreen)
    SignallingRef.of[IO, S](init).flatMap { stateSignal =>
      val renderLoop = stateSignal.continuous
        .map(render)
        .metered(20.millis)

      val actions = fs2.io
        .stdinUtf8[IO](1024)
        .map(_.trim)
        .mapFilter(input)
        .map(input =>
          action(input, stateSignal).adaptError { case e =>
            enrichError(input)(e)
          }
        )
        .parJoinUnbounded
      renderLoop
        .concurrently(actions)
        .concurrently(simulation(stateSignal))
        .animateToIO(frame)
    }
  }

  private def enrichError(input: C)(error: Throwable): Throwable = new GameAppWithError.ActionError(input, ???, error)
}

object GameAppWithError {
  final class ActionError[S, C](input: C, state: S, error: Throwable)
      extends Exception(s"Error occurred in action for input $input and state $state.", error)
}

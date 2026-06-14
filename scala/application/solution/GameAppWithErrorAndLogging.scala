import cats.effect.*
import fs2.*
import fs2.concurrent.Channel
import cats.effect.*
import doodle.java2d.*
import fs2.concurrent.*
import scala.concurrent.duration.*
import cats.syntax.all.*
import doodle.interact.*
import doodle.interact.syntax.all.*
import doodle.core.*
import fs2.io.file.Path
import cats.effect.kernel.Resource.ExitCase

trait GameAppWithErrorAndLogging[S, C] extends IOApp.Simple {

  def game: IO[Game[S, C]]

  def run: IO[Unit] = game.flatMap { game =>
    val frame = Frame.default.withSize(600, 600).withBackground(Color.paleGreen)
    SignallingRef.of[IO, S](game.init).flatMap { stateSignal =>
      val loop =
        GameAppWithErrorAndLogging.Logger("game.log").flatMap { logger =>
          val renderLoop = stateSignal.continuous
            .map(game.render)
            .metered(20.millis)

          val actions = fs2.io
            .stdinUtf8[IO](1024)
            .map(_.trim)
            .mapFilter(game.input)
            .map(input =>
              game
                .action(input, stateSignal)
                .adaptError { case e =>
                  enrichError(input)(e)
                }
                .onFinalizeCase {
                  case ExitCase.Succeeded =>
                    logger.info(s"Action with input $input succeeded.")
                  case ExitCase.Errored(err) =>
                    logger.info(s"Action with input $input errored ${err}.")
                  case ExitCase.Canceled =>
                    logger.info(s"Action with input $input was canceled.")
                }
            )
            .parJoinUnbounded
          renderLoop
            .concurrently(actions)
            .concurrently(game.simulation(stateSignal))
        }
      loop.animateToIO(frame)
    }
  }

  private def enrichError(input: C)(error: Throwable): Throwable =
    new GameAppWithErrorAndLogging.ActionError(input, error)
}

object GameAppWithErrorAndLogging {

  trait Logger {
    def info(msg: String): IO[Unit]
  }

  object Logger {
    def apply(filename: String): Stream[IO, Logger] = {
      val writePipe = fs2.io.file.Files[IO].writeUtf8(Path(filename))
      Stream.eval(Channel.synchronous[IO, String]).flatMap { channel =>
        val logger = new Logger {
          def info(msg: String): IO[Unit] =
            channel.send(s"${msg}${System.lineSeparator()}").void
        }
        Stream(logger).concurrently(channel.stream.through(writePipe))
      }
    }
  }

  final class ActionError[S, C](input: C, error: Throwable)
      extends Exception(
        s"Error occurred in action for input $input.",
        error
      )
}

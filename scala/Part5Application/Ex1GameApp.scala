import cats.effect.*
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

// Game engine code
trait GameApp[S, C] extends IOApp.Simple {

  def game: IO[Game[S, C]]

  // Implement this function
  def run: IO[Unit] = {
    game.flatMap { gameValue =>
    val frame = Frame.default.withSize(600, 600).withBackground(Color.paleGreen)

    SignallingRef.of[IO, S](gameValue.init)
      .flatMap { gameStateRef =>
        val renderLoop = gameStateRef
          .discrete
          .map(gameValue.render)

        val eventLoop =
          Stream.eval(IO.readLine)
            .repeat
            .mapFilter(gameValue.input)
            .map { command =>
          val actionStream = gameValue.action(command, gameStateRef)
          actionStream
        }.parJoinUnbounded

        val simulationLoop = gameValue.simulation(gameStateRef)
        Stream(renderLoop, simulationLoop, eventLoop)
        .parJoinUnbounded
        .animateToIO(frame)

      }
    }

  }
}

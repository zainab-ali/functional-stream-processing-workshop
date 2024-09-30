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

  def game: Game[S, C]

  def run: IO[Unit] = {
    val g = game
    import g.*
    val frame = Frame.default.withSize(600, 600).withBackground(Color.paleGreen)
    SignallingRef.of[IO, S](init).flatMap { stateSignal =>
      val renderLoop = stateSignal.continuous
        .map(render)
        .metered(20.millis)

      val actions = fs2.io
        .stdinUtf8[IO](1024)
        .map(_.trim)
        .mapFilter(input)
        .map(action(_, stateSignal))
        .parJoinUnbounded
      renderLoop
        .concurrently(actions)
        .concurrently(simulation(stateSignal))
        .animateToIO(frame)
    }
  }
}

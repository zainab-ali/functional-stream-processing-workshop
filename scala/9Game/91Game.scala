import fs2.*
import cats.effect.*
import doodle.java2d.*
import fs2.concurrent.*
import scala.concurrent.duration.*
import cats.syntax.all.*
import doodle.interact.*
import doodle.interact.syntax.all.*
import doodle.core.*

trait Game[S, C] {
  def init: S
  def input(text: String): Option[C]
  def action(command: C, state: Ref[IO, S]): Stream[IO, Nothing]
  def render(state: S): Picture[Unit]
  def simulation(ref: Ref[IO, S]): Stream[IO, Nothing]

  def run: IO[Unit] = {
    val frame = Frame.default.withSize(600, 600).withBackground(Color.paleGreen)
    SignallingRef.of[IO, S](init).flatMap { stateSignal =>
      val renderLoop = stateSignal.continuous
        .map(render)
        .metered(20.millis)

      val actions = fs2.io
        .stdin[IO](1024)
        .split(_ == '\n'.toByte)
        .map(chk => String(chk.toArray))
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

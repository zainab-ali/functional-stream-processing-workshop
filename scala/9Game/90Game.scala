import fs2.*
import cats.effect.*
import doodle.java2d.*

trait Game0[S, C] {
  def init: S
  def input(text: String): Option[C]
  def action(command: C, state: Ref[IO, S]): Stream[IO, Nothing]
  def render(state: S): Picture[Unit]
  def simulation(ref: Ref[IO, S]): Stream[IO, Nothing]
}

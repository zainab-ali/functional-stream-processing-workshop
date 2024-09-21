import fs2.*
import cats.effect.IO
import aquascape.*

/* We can use aquascape to get a feel for how operators let us compose iterative programs. */
object Example03 extends AquascapeApp {
  def name: String = "evaluation"
  def stream(using Scape[IO]): IO[Unit] = {
    Stream(1, 2, 3)
      .stage("Source")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

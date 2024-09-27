import fs2.*
import cats.effect.IO
import aquascape.*
import aquascape.drawing.Config

/* We can use aquascape to get a feel for how operators let us compose iterative programs. */
object Example04 extends AquascapeApp {
  override def config: Config = super.config.scale(4)
  def name: String = "04Evaluation"
  def stream(using Scape[IO]): IO[Unit] = {
    Stream(1, 2, 3)
      .stage("Source")
      .compile
      .toList
      .compileStage("compile.toList")
      .void
  }
}

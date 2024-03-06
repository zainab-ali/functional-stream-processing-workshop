import fs2.*
import cats.effect.*
import aquascape.*

/* We can use aquascape to get a feel for how operators let us compose iterative programs. */
object Example03 extends AquascapeApp.Simple {
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Source")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

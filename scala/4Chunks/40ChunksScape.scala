import fs2.*
import cats.effect.*
import aquascape.*

/* fs2 operates on streams in chunks. */
object Example40 extends AquascapeApp {

  def name: String = "chunks"

  def stream(using Scape[IO]) = {
    /* Tell aquascape to draw chunks */
    Stream(1, 2, 3)
      .stage("Source")
      .take(2)
      .stage("take(2)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

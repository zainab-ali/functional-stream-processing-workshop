import fs2.*
import cats.effect.*
import aquascape.*

object BasicScape extends AquascapeApp {
  def name: String = "basicScape"
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Source")
      // .take(2)
      // .stage("take(2)")
      // .drop(1)
      // .stage("drop(1)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

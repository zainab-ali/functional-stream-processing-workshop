import fs2.*
import cats.effect.*
import aquascape.*

object Example53 extends AquascapeApp {

  def name: String = "combining-merge"
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Left", branch = "left")
      .fork("root", "left")
      .merge(
        Stream(4, 5, 6)
          .stage("Right", branch = "right")
          .fork("root", "right")
      )
      .stage("merge")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

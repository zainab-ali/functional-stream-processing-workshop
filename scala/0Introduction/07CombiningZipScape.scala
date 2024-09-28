import fs2.*
import cats.effect.*
import aquascape.*

object Example07 extends AquascapeApp {

  def name: String = "combining-zip"
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Left")
      .zip(
        Stream(4, 5, 6)
          .stage("Right")
      )
      .stage("zip")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}
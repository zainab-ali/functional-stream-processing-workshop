import fs2.*
import cats.effect.IO
import aquascape.*

object Example52 extends AquascapeApp {

  def name: String = "combining-append"
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Left")
      .append(
        Stream(4, 5, 6)
          .stage("Right")
      )
      .stage("append")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

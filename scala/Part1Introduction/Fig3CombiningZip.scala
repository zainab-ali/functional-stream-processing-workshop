import fs2.*
import cats.effect.*
import aquascape.*

object Fig3CombiningZip extends WorkshopAquascapeApp {
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

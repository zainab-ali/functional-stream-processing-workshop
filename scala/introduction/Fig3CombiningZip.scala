import fs2.*
import cats.effect.*
import aquascape.*

object Fig3CombiningZip extends WorkshopAquascapeApp {
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Stage(1, 2, 3)")
      .zip(
        Stream(4, 5, 6)
          .stage("Stage(4, 5, 6)")
      )
      .stage("zip(…)")
      .compile
      .count
      .compileStage("compile.count")
      .void
  }
}

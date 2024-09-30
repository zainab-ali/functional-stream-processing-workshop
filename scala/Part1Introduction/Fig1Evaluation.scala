import fs2.*
import cats.effect.IO
import aquascape.*

object Fig1Evaluation extends WorkshopAquascapeApp {
  def stream(using Scape[IO]): IO[Unit] = {
    Stream(1, 2, 3)
      .stage("Source")
      .compile
      .toList
      .compileStage("compile.toList")
      .void
  }
}

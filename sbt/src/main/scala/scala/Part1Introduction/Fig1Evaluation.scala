import fs2.*
import cats.effect.IO
import aquascape.*

object Fig1Evaluation extends WorkshopAquascapeApp {
  def stream(using Scape[IO]): IO[Unit] = {
    Stream(1)
      .stage("Stream(1)")
      .repeat
      .stage("repeat")
      .take(2)
      .stage("take(2)")
      .compile
      .count
      .compileStage("compile.count")
      .void
  }
}

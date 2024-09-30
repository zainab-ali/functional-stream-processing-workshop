import fs2.*
import cats.effect.*
import aquascape.*

object Fig2Take extends WorkshopAquascapeApp {
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Stream(1,2,3)")
      .take(2)
      .stage("take(2)")
      .drop(1)
      .stage("drop(1)")
      .compile
      .toList
      .compileStage("compile.toList")
      .void
  }
}

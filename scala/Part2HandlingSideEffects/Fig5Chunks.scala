import fs2.*
import cats.effect.*
import aquascape.*

object Fig5Chunks extends WorkshopAquascapeApp {

  override def chunked: Boolean = true

  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Stream(1, 2, 3)")
      .take(2)
      .stage("take(2)")
      .compile
      .toList
      .compileStage("compile.count")
      .void
  }
}

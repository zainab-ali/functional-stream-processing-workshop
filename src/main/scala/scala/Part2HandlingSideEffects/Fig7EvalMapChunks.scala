import fs2.*
import cats.effect.*
import aquascape.*

object Fig7EvalMapChunks extends WorkshopAquascapeApp {
  override def chunked: Boolean = true

  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Source")
      .evalMapChunk(x => print(x).trace())
      .stage("evalMapChunk")
      .take(2)
      .stage("take(2)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

import fs2.*
import cats.effect.*
import aquascape.*

object Fig6EvalMap extends WorkshopAquascapeApp {
  def chunked: Boolean = true

  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Source")
      .evalMap(x => print(x).trace())
      .stage("evalMap")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

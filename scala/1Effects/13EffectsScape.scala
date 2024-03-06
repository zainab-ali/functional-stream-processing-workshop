import fs2.*
import cats.effect.*
import aquascape.*

object Example13 extends AquascapeApp.Simple.File("effects") {

  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Source")
      /* trace draws the side-effect's output as part of our scape. */
      .evalMap(x => print(x).trace())
      .stage("evalMap")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

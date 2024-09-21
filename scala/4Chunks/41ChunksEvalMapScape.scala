import fs2.*
import cats.effect.*
import aquascape.*

/* The chunks outputted by evalMap have a single element only. This ensures that effects are not evaluated multiple times. */
object Example41 extends AquascapeApp {
  def name: String = "chunks-evalMap"

  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def stream(using Scape[IO]) = {
    /* Tell aquascape to draw chunks */
    Stream(1, 2, 3)
      .stage("Source")
      .evalMap(x => print(x).trace())
      .stage("evalMap")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

import fs2.*
import cats.effect.*
import aquascape.*

/* evalMapChunk preserves chunks, but at the cost of executing more effects than expressed. */
object Example42 extends AquascapeApp {
  def name: String = "chunks-evalMapChunk"

  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)

  def stream(using Scape[IO]) = {
    /* Tell aquascape to draw chunks */
    Stream(1, 2, 3)
      .stage("Source")
      .evalMapChunk(x => print(x).trace())
      .stage("evalMapChunk")
      /* Note that 3 is printed even though only 2 elements are taken. */
      .take(2)
      .stage("take(2)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

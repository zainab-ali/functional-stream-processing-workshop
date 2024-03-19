import fs2.*
import cats.effect.*
import aquascape.*

/* We can introduce more operators and draw a stage for each operator. */
object Example04 extends AquascapeApp.Simple {
  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Stream(1,2,3)")
      .take(2)
      .stage("take(2)")
      .drop(1)
      .stage("drop(1)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

import fs2.*
import cats.effect.*
import aquascape.*
import cats.syntax.all.*

object Fig2Errors extends WorkshopAquascapeApp {
  def raiseIfTwo(x: Int): IO[Int] = IO.raiseError(new Error("!")).whenA(x == 2).as(x)

  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Stream(1, 2, 3)")
      .evalTap(x => raiseIfTwo(x).trace())
      .stage("evalTap(raiseIfTwo)")
      .compile
      .count
      .compileStage("compile.count")
      .attempt
      .void
  }
}

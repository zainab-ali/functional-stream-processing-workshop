import fs2.*
import cats.effect.*
import aquascape.*
import cats.syntax.all.*

object Fig3HandleErrorWith extends WorkshopAquascapeApp {
  def raiseIfTwo(x: Int): IO[Int] = IO.raiseError(new Error("!")).whenA(x == 2).as(x)

  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Stream(1, 2, 3)")
      .evalMap(x => raiseIfTwo(x).trace())
      .stage("evalMap(raiseIfTwo)")
      .handleErrorWith(_ => Stream(42).stage("Stream(42)"))
      .stage("handleErrorWith(…)")
      .compile
      .toList
      .compileStage("compile.toList")
      .void
  }
}

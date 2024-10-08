import fs2.*
import cats.effect.*
import aquascape.*
import cats.syntax.all.*

object Fig3HandleErrorWith extends WorkshopAquascapeApp {
  def error(x: Int): IO[Int] = IO.raiseError(new Error("!")).whenA(x == 2).as(x)

  def stream(using Scape[IO]) = {
    Stream(1, 2, 3)
      .stage("Source")
      .evalMap(x => error(x).trace())
      .stage("evalMap")
      .handleErrorWith(_ => Stream(42).stage("handler"))
      .stage("handleErrorWith")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

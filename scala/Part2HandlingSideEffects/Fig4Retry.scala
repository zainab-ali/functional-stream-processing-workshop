import fs2.*
import cats.effect.*
import aquascape.*
import cats.syntax.all.*

object Fig4Retry extends WorkshopAquascapeApp {

  def print(x: Int): IO[String] = IO.println(s"Printing $x").as(x.toString)
  def error(x: Int): IO[Int] = IO.raiseError(new Error("!")).whenA(x == 2).as(x)

  def stream(using Scape[IO]) = {
    def numbers(n: Int): Stream[IO, Int] = Stream(1, 2, 3)
      .stage(s"Source-$n")
      .evalMap(x => error(x).trace())
      .stage(s"evalMap-$n")
      .handleErrorWith(_ => numbers(n + 1))
      .stage(s"handleErrorWith-$n")

    numbers(0)
      .take(2)
      .stage("take")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

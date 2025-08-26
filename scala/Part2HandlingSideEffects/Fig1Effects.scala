import fs2.*
import cats.effect.*
import aquascape.*

object Fig1Effects extends WorkshopAquascapeApp {
  def greeting(name: String): IO[String] =
    IO.println(s"Hi $name!").as(s"Hi $name!")

  def stream(using Scape[IO]) = {
    Stream("Mao", "Owl")
      .stage("Stream(Mao, Owl)")
      /* trace draws the side-effect's output as part of our scape. */
      .evalMap(x => greeting(x).trace().void)
      .stage("evalMap(…)")
      .head
      .compile
      .drain
      .compileStage("head.compile.drain")
  }
}

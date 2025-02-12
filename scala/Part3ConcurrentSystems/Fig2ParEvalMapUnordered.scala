import fs2.*
import cats.effect.*
import aquascape.*
import scala.concurrent.duration.*

object Fig2ParEvalMapUnordered extends WorkshopAquascapeApp {
 tasksAAndC
      .parEvalMapUnordered(3)(eval(startTime))
      .evalMap(sleepFixed(startTime))
  def process(time: Int, i: Long): IO[Long] = IO.sleep(time.seconds).as(i)

  def stream(using Scape[IO]) = {
    Stream(5, 4, 3, 2, 1).zipWithIndex
      .covary[IO]
      .stage("Source", "upstream")
      .fork("root", "upstream")
      .parEvalMapUnordered(3)((t, i) => process(t, i).trace())
      .stage("parEvalMapUnordered(3)(…)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

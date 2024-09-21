import fs2.*
import cats.effect.*
import aquascape.*
import scala.concurrent.duration.*

object Example62 extends AquascapeApp {

  def name: String = "parEvalMap"
  def eval(time: Int, i: Long): IO[Long] = IO.println(
    s"Starting to process $i ($time seconds)"
  ) >> IO.sleep(time.seconds) >> (IO.println(s"Processed $i")).as(i)

  def stream(using Scape[IO]) = {
    Stream(5, 4, 3, 2, 1).zipWithIndex
      .covary[IO]
      .stage("Source", "upstream")
      .fork("root", "upstream")
      .parEvalMap(3)((t, i) => eval(t, i).trace())
      .stage("parEvalMap(3)(…)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

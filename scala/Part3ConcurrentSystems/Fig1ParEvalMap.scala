import fs2.*
import cats.effect.*
import aquascape.*
import scala.concurrent.duration.*

object Fig1ParEvalMap extends WorkshopAquascapeApp {

  final case class Task(name: String, time: Int)

  val tasks =
    Stream(Task("a", 5), Task("b", 4), Task("c", 3), Task("d", 2), Task("e", 1))

  def process(startTime: FiniteDuration)(task: Task): IO[Task] =
    IO.sleep(task.time.seconds).as(task)

  given cats.Show[Task] = _.name
  def stream(using Scape[IO]) = {
    Clock[IO].realTime.flatMap { startTime =>
      tasks
        .covary[IO]
        .stage("tasks", "upstream")
        .fork("root", "upstream")
        .parEvalMap(3)(t => process(startTime)(t).trace())
        .stage("parEvalMap(3)(…)")
        .compile
        .drain
        .compileStage("compile.drain")
    }
  }
}

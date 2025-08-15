import fs2.*
import cats.effect.*
import cats.Show
import aquascape.*
import scala.concurrent.duration.*

object Fig4Order extends WorkshopAquascapeApp {

  def eval(task: Task): IO[Task] = IO.sleep(task.time.seconds).as(task)
  def sleepFixed(i: Task): IO[Task] = IO.sleep(2.seconds).as(i)

  given Show[Task] = task => task.id.toString

  case class Task(id: Char, time: Int)

  def stream(using Scape[IO]) = {
    Stream(Task('a', 5), Task('c', 3))
      .covary[IO]
      .stage("Stream(Task('a', 5), Task('c', 3))", "upstream")
      .fork("root", "upstream")
      .parEvalMapUnordered(3)(t => eval(t).trace())
      .stage("parEvalMapUnordered(3)(eval)")
      .evalMap(sleepFixed)
      .stage("evalMap(sleepFixed)")
      .compile
      .drain
      .compileStage("compile.drain")
  }
}

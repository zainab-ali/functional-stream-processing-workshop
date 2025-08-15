

final class Code1Concurrency$_ {
def args = Code1Concurrency_sc.args$
def scriptPath = """Code1Concurrency.sc"""
/*<script>*/
import fs2.*
import scala.concurrent.duration.*
import cats.effect.IO
import cats.effect.kernel.Clock
import cats.effect.unsafe.implicits.global

final case class Task(name: String, time: Int)

val time = Stream.eval(Clock[IO].realTime)
def printProcessed(startTime: FiniteDuration, task: Task): IO[Unit] =
  Clock[IO].realTime.flatMap(currentTime =>
    IO.println(
      s"Processed ${task.name} at ${(currentTime - startTime).toSeconds}s"
    )
  )

def printStarting(startTime: FiniteDuration, task: Task): IO[Unit] =
  Clock[IO].realTime.flatMap(currentTime =>
    IO.println(
      s"Starting to process ${task.name} at ${(currentTime - startTime).toSeconds}s"
    )
  )

def process(startTime: FiniteDuration)(task: Task): IO[Task] =
  printStarting(startTime, task).bracket[Task](_ =>
    IO.sleep(task.time.seconds).as(task)
  )(_ => printProcessed(startTime, task))

val tasks =
  Stream(Task("a", 5), Task("b", 4), Task("c", 3), Task("d", 2), Task("e", 1))

time
  .flatMap { startTime => tasks.evalMap(process(startTime)) }
  .compile
  .drain
  .unsafeRunSync()

time
  .flatMap { startTime =>
    tasks
      .parEvalMap(3)(process(startTime))
  }
  .compile
  .toList
  .unsafeRunSync()

time
  .flatMap { startTime =>
    tasks
      .parEvalMapUnordered(3)(process(startTime))
  }
  .compile
  .toList
  .unsafeRunSync()

def processConstantTime(startTime: FiniteDuration)(task: Task): IO[Task] =
  printStarting(startTime, task).bracket[Task](_ =>
    IO.sleep(2.seconds).as(task)
  )(_ => printProcessed(startTime, task))

time
  .flatMap { startTime =>
    tasks
      .parEvalMap(3)(processConstantTime(startTime))
  }
  .compile
  .toList
  .unsafeRunSync()

time
  .flatMap { startTime =>
    tasks
      .parEvalMapUnordered(3)(process(startTime))
      .take(3)
  }
  .compile
  .drain
  .unsafeRunSync()

val tasksAAndC = Stream(Task("a", 5), Task("c", 3))
time
  .flatMap { start =>
    tasksAAndC
      .parEvalMapUnordered(3)(process(start))
      .evalMap(processConstantTime(start))
  }
  .compile
  .drain
  .unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code1Concurrency_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code1Concurrency$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code1Concurrency_sc.script as `Code1Concurrency`


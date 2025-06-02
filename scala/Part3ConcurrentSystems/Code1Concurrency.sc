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

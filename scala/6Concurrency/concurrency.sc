import cats.effect.kernel.Clock
import fs2._
import cats.effect.IO
import cats.syntax.all._
import cats.effect.unsafe.implicits.global

import scala.concurrent.duration.*

val time = Stream.eval(Clock[IO].realTime)

final case class Task(name: String, time: Int)

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

def eval(startTime: FiniteDuration)(task: Task): IO[Task] =
  printStarting(startTime, task).bracket(_ => IO.sleep(
    task.time.seconds
  ))(_ => printProcessed(startTime, task)).as(task)

val tasks =
  Stream(Task("a", 5), Task("b", 4), Task("c", 3), Task("d", 2), Task("e", 1))

time
  .flatMap { startTime => tasks.evalMap(eval(startTime)) }
  .compile
  .drain
  .unsafeRunSync()

time
  .flatMap { startTime =>
    tasks
      .parEvalMap(3)(eval(startTime))
  }
  .compile
  .toList
  .unsafeRunSync()

time
  .flatMap { startTime =>
    tasks
      .parEvalMapUnordered(3)(eval(startTime))
  }
  .compile
  .toList
  .unsafeRunSync()

def sleepFixed(startTime: FiniteDuration)(task: Task): IO[Task] =
  printStarting(startTime, task) >> IO.sleep(20.milliseconds) >> printProcessed(
    startTime,
    task
  ).as(task)

time
  .flatMap { startTime =>
    tasks
      .parEvalMap(3)(sleepFixed(startTime))
  }
  .compile
  .drain
  .unsafeRunSync()

time
  .flatMap { startTime =>
    tasks
      .parEvalMapUnordered(3)(eval(startTime))
      .take(3)
  }
  .compile
  .drain
  .unsafeRunSync()

val tasksAAndC = Stream(Task("a", 5), Task("c", 3))
time
  .flatMap { startTime =>
    tasksAAndC
      .parEvalMapUnordered(3)(eval(startTime))
      .evalMap(sleepFixed(startTime))
  }
  .compile
  .drain
  .unsafeRunSync()

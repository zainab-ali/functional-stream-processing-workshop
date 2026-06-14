import cats.effect.unsafe.implicits.global

import fs2._
import cats.effect.IO
import cats.syntax.all._
import cats.effect.unsafe.implicits.global
import scala.concurrent.duration.*

import cats.effect.Ref

val counter = Ref.of[IO, Int](0).unsafeRunSync()

counter.get.unsafeRunSync()

counter
  .set(42)
  .flatMap(_ => counter.get)
  .unsafeRunSync()

counter
  .update(_ + 42)
  .flatMap(_ => counter.get)
  .unsafeRunSync()

val incrementEverySecond =
  Stream.eval(counter.update(_ + 1)).repeat.spaced(1.second)

val printTwicePerSecond =
  Stream.eval(counter.get.flatMap(IO.println)).repeat.spaced(500.millis)

incrementEverySecond
  .take(3)
  .concurrently(printTwicePerSecond)
  .compile
  .drain
  .unsafeRunSync()

import fs2.concurrent.*

SignallingRef
  .of[IO, Int](0)
  .flatMap { counter =>
    val incrementEverySecond =
      Stream.repeatEval(counter.update(_ + 1)).spaced(1.second)

    val printOnChange = counter.discrete.evalMap(IO.println)
    incrementEverySecond
      .take(3)
      .concurrently(printOnChange)
      .compile
      .drain
  }
  .unsafeRunSync()

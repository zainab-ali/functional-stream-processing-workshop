import fs2.*
import scala.concurrent.duration.*
import cats.effect.IO
import cats.effect.Ref
import cats.effect.unsafe.implicits.global

/** A ref can be used to safely share state */
val counter = Ref.of[IO, Int](0).unsafeRunSync()

/** It has get, set and update functions */
counter.get.unsafeRunSync()

counter
  .set(42)
  .flatMap(_ => counter.get)
  .unsafeRunSync()

counter
  .update(_ + 42)
  .flatMap(_ => counter.get)
  .unsafeRunSync()

/** It can be updated as part of a stream */
val incrementEverySecond =
  Stream.eval(counter.update(_ + 1)).repeat.spaced(1.second)

incrementEverySecond
  .take(3)
  .compile
  .drain
  .flatMap(_ => counter.get)
  .unsafeRunSync()

/** It can be updated and accessed concurrently */
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

SignallingRef
  .of[IO, Int](0)
  .flatMap { counter =>
    val incrementEverySecond =
      Stream.repeatEval(counter.update(_ + 1)).spaced(1.second)
    val printTwicePerSecond =
      counter.continuous.spaced(500.millis).evalMap(IO.println)

    incrementEverySecond
      .take(3)
      .concurrently(
        printTwicePerSecond
      )
      .compile
      .drain
  }
  .unsafeRunSync()

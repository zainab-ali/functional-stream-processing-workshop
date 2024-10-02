package Part3ConcurrentSystems


final class Code2ConcurrentState$_ {
def args = Code2ConcurrentState_sc.args$
def scriptPath = """Part3ConcurrentSystems/Code2ConcurrentState.sc"""
/*<script>*/
import fs2.*
import scala.concurrent.duration.*
import cats.effect.IO
import cats.effect.Ref
import cats.effect.unsafe.implicits.global

val counter = Ref.of[IO, Int](0).unsafeRunSync()

Ref
  .of[IO, Int](0)
  .flatMap { counter =>
    counter.set(42).flatMap(_ => counter.get)
  }
  .unsafeRunSync()

Ref
  .of[IO, Int](0)
  .flatMap { counter =>
    counter.update(_ + 42).flatMap(_ => counter.get)
  }
  .unsafeRunSync()

Ref
  .of[IO, Int](0)
  .flatMap { counter =>
    val sleepAndIncrement =
      Stream.sleep[IO](1.second) ++ Stream.eval(counter.update(_ + 1))
    sleepAndIncrement.repeat.take(3).compile.drain.flatMap(_ => counter.get)
  }
  .unsafeRunSync()

Ref
  .of[IO, Int](0)
  .flatMap { counter =>
    val sleepAndIncrement =
      Stream.sleep[IO](1.second) ++ Stream.exec(counter.update(_ + 1))
    val sleepAndPrint = Stream.sleep[IO](500.milliseconds) ++ Stream.eval(
      counter.get.flatMap(IO.println)
    )
    sleepAndIncrement
      .repeatN(3)
      .concurrently(sleepAndPrint.repeat)
      .compile
      .drain
  }
  .unsafeRunSync()

import fs2.concurrent.*

SignallingRef.of[IO, Int](0).flatMap { signallingRef =>
  val increment =
    Stream.repeatEval(signallingRef.update(_ + 1)).spaced(1.second).take(3)

  increment
    .concurrently(signallingRef.discrete.evalMap(IO.println))
    .compile
    .drain
}.unsafeRunSync()

SignallingRef.of[IO, Int](0).flatMap { signallingRef =>
  val increment =
    Stream.repeatEval(signallingRef.update(_ + 1)).spaced(1.second).take(3)

  increment
    .concurrently(signallingRef.continuous.spaced(500.millis).evalMap(IO.println))
    .compile
    .drain
}.unsafeRunSync()


SignallingRef.of[IO, Int](0).flatMap { signallingRef =>
  val increment = Stream.repeatEval(signallingRef.update(_ + 1)).take(3) ++ Stream.unit.repeat
  val print = signallingRef.continuous.evalMap(IO.println).take(5)
  print.interleave(increment).compile.drain
}.unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code2ConcurrentState_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code2ConcurrentState$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code2ConcurrentState_sc.script as `Code2ConcurrentState`




final class Code1Channel$_ {
def args = Code1Channel_sc.args$
def scriptPath = """Code1Channel.sc"""
/*<script>*/
import fs2.*
import fs2.concurrent.*
import cats.effect.*
import cats.effect.unsafe.implicits.global
import scala.concurrent.duration.*

Channel.unbounded[IO, String]

Stream
  .iterate(0)(_ + 1)
  .evalMap(i => IO.println(s"Producing $i"))
  .metered(1.second)

Channel.unbounded[IO, Int].unsafeRunSync()

Channel
  .unbounded[IO, Int]
  .flatMap { channel =>
    val producer = Stream
      .iterate(0)(_ + 1)
      .evalTap(i => IO.println(s"Producing $i"))
      .metered(1.second)
      .through(channel.sendAll)
    val consumer = channel.stream.evalMap(i => IO.println(s"Consuming $i"))
    consumer.concurrently(producer).compile.drain
  }
  .timeout(3.second)
  .unsafeRunSync()

Channel
  .unbounded[IO, Int]
  .flatMap { channel =>
    val producer = Stream
      .iterate(0)(_ + 1)
      .evalTap(i => IO.println(s"Producing $i"))
      .metered(1.second)
      .through(channel.sendAll)
    val consumer = channel.stream
      .evalMap(i => IO.println(s"Consuming $i"))
      .metered(2.seconds)
    consumer.concurrently(producer).compile.drain
  }
  .timeout(10.second)
  .unsafeRunSync()

Channel
  .bounded[IO, Int](2)
  .flatMap { channel =>
    Stream
      .range(0, Int.MaxValue)
      .debug()
      .through(channel.sendAll)
      .compile
      .drain
  }
  .timeout(10.second)
  .unsafeRunSync()

Channel
  .bounded[IO, Int](2)
  .flatMap { channel =>
    val producer = Stream
      .iterate(0)(_ + 1)
      .evalTap(i => IO.println(s"Producing $i"))
      .metered(1.second)
      .through(channel.sendAll)
    val consumer = channel.stream
      .evalMap(i => IO.println(s"Consuming $i"))
      .metered(2.seconds)
    consumer.concurrently(producer).compile.drain
  }
  .timeout(20.second)
  .unsafeRunSync()

Channel
  .bounded[IO, Int](2)
  .flatMap { channel =>
    val producer = Stream
      .iterate(0)(_ + 1)
      .evalTap(i => IO.println(s"Producing $i"))
      .metered(1.second)
      .through(channel.sendAll)
    val consumer = channel.stream
      .evalMap(i => IO.println(s"Consuming $i"))
      .metered(2.seconds)
    consumer.concurrently(producer).compile.drain
  }
  .timeout(20.second)
  .unsafeRunSync()

Channel
  .bounded[IO, Int](2)
  .flatMap { channel =>
    val producer = Stream
      .iterate(0)(_ + 1)
      .take(3)
      .evalTap(i => IO.println(s"Producing $i"))
      .metered(1.second)
      .through(channel.sendAll)
    val consumer = channel.stream
      .evalMap(i => IO.println(s"Consuming $i"))
      .metered(2.seconds)
    consumer.concurrently(producer).compile.drain
  }
  .timeout(20.second)
  .unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code1Channel_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code1Channel$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code1Channel_sc.script as `Code1Channel`


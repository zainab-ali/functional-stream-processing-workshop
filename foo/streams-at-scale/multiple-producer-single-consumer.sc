import fs2.*
import cats.effect.*
import scala.concurrent.duration.*

import fs2.concurrent.*

Channel.unbounded[IO, String]

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
  .timeout(3.seconds)
  .attempt.unsafeRunSync()

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
  .timeout(10.seconds)
  .attempt.unsafeRunSync()

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
  .timeout(10.seconds)
  .attempt.unsafeRunSync()

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
  .attempt.unsafeRunSync()
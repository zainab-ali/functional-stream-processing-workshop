import cats.effect.*
import cats.effect.unsafe.implicits.global

val greetingIO = IO.println("Hello!")

import fs2.*

val greetingStream = Stream.eval(greetingIO)

val greetThriceStream = greetingStream.repeat.take(3)

val greetAndCountIO: IO[Long] = greetThriceStream.compile.count

greetAndCountIO.unsafeRunSync()

val greetThriceIO: IO[Unit] = greetThriceStream.compile.drain
greetThriceIO.unsafeRunSync()

Stream("Mao", "Owl")
  .evalMap(name => IO.println(s"Hi $name!"))
  .head
  .compile
  .drain

val count: Long = Stream(1, 2, 3).compile.count
val countIO: IO[Long] = Stream(1, 2, 3).evalMap(IO.println(_)).compile.count

Stream.exec(greetingIO)

Stream(1, 2, 3)
  .evalMap(n => IO.println(s"The number is $n."))
  .compile
  .count
  .unsafeRunSync()

import fs2.io.file.*

Files[IO].list(Path("data")).compile.toList.unsafeRunSync()

Files[IO]
  .readUtf8Lines(Path("data/hard-times.txt"))
  .take(10)
  .compile
  .toList
  .unsafeRunSync()

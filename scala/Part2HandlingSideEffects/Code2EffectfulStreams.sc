import cats.effect.*
import cats.effect.unsafe.implicits.global

val greetingIO = IO.println("Hello!")

import fs2.*

val greetingStream = Stream.eval(greetingIO)

greetingStream.compile.count.unsafeRunSync()

greetingStream.compile.drain.unsafeRunSync()

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

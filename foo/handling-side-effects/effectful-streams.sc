import cats.effect.IO

val greetingIO = IO.println("Hello!")

import fs2._

val greetingStream = Stream.eval(greetingIO)

val greetThriceStream = greetingStream.repeat.take(3)

val greetAndCountIO: IO[Long] = greetThriceStream.compile.count

greetAndCountIO.unsafeRunSync()

val greetThriceIO: IO[Unit] = greetThriceStream.compile.drain
greetThriceIO.unsafeRunSync()

Stream("Mao", "Owl")
  .evalMap(name => IO.println(s"Hi $name!"))
  .compile
  .drain.unsafeRunSync()

Stream("Mao", "Owl")
  .evalMap(name => IO.println(s"Hi $name!"))
  .head
  .compile
  .drain

val count: Long = Stream(1,2,3).compile.count
val countIO: IO[Long] = Stream(1,2,3).evalMap(IO.println(_)).compile.count

Stream.exec(greetingIO)
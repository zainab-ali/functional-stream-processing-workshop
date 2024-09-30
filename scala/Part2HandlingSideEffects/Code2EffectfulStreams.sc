import cats.effect.*
import cats.effect.unsafe.implicits.global

val greetingIO = IO.println("Hello!")

import fs2.*
val greetingStream = Stream.eval(greetingIO)

Stream.exec(greetingIO)

Stream(1, 2, 3)
  .evalMap(n => IO.println(s"The number is $n."))
  .compile
  .count
  .unsafeRunSync()

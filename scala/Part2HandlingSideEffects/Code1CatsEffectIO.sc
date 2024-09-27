val greeting: Unit = println("Hello!")

import cats.effect.IO

val greetingIO0 = IO(println("Hello!"))

val greetingIO = IO.println("Hello!")

val greetTwice =
  greetingIO.flatMap(_ => greetingIO)

import cats.effect.unsafe.implicits.global
greetTwice.unsafeRunSync()

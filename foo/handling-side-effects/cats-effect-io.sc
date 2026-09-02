val greeting: Unit = println("Hello!")

// import cats.effect.IO
// val greetingIO = IO(println("Hello!"))

import cats.effect.IO

val greetingIO = IO.println("Hello!")

// import cats.effect.IOApp
// 
// object App extends IOApp.Simple {
//   def run: IO[Unit] = greetingIO
// }

import cats.effect.unsafe.implicits.global

greetingIO.unsafeRunSync()

val greetTwiceIO =
  greetingIO.flatMap(_ => greetingIO)

greetTwiceIO.unsafeRunSync()

val greetThriceIO =
  greetingIO.flatMap(_ => greetingIO)
            .flatMap(_ => greetingIO)
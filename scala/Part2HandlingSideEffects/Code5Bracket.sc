import fs2.*
import cats.effect.IO
import cats.effect.unsafe.implicits.*
import scala.util.control.NoStackTrace

object BOOM extends Throwable("BOOM!") with NoStackTrace

case class Tap(temp: String)

def openTap(temp: String): IO[Tap] =
  IO.println(s"Opening $temp tap.").as(Tap(temp))

def closeTap(tap: Tap): IO[Unit] =
  IO.println(s"Closing ${tap.temp} tap.")

def drawWater(tap: Tap): IO[Unit] =
  IO.println(s"Drawing water from ${tap.temp} tap.")

openTap("hot").flatMap { tap =>
  drawWater(tap).attempt
    .flatTap(_ => closeTap(tap))
    .rethrow
}

val tapStream = Stream.bracket(openTap("hot"))(closeTap)

tapStream.compile.toList.unsafeRunSync()

tapStream
  .evalMap(drawWater)
  .compile
  .drain
  .unsafeRunSync()

def drawTwice(tap: Tap) = Stream.eval(drawWater(tap)).repeat.take(2)

tapStream
  .flatMap(drawTwice)
  .compile
  .drain
  .unsafeRunSync()

Stream("hot", "cold")
  .flatMap { temp =>
    Stream
      .bracket(openTap(temp))(closeTap)
      .flatMap(drawTwice)
  }
  .compile
  .drain
  .unsafeRunSync()

tapStream
  .evalMap(_ => IO.raiseError(BOOM))
  .compile
  .drain

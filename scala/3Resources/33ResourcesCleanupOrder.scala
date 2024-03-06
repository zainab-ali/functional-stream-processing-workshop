import fs2.*
import cats.effect.{Trace => _, *}

/* The resource release is executed when the stream is done. */
object Example33 extends IOApp.Simple {
  enum Temperature:
    case Hot
    case Cold

  case class Tap(temp: Temperature)
  case class Water(temp: Temperature)

  def make(temp: Temperature): Resource[IO, Tap] =
    Resource.make(IO.println(s"Opening $temp tap.").as(Tap(temp)))(_ =>
      IO.println(s"Closing $temp tap.")
    )
  def drawOne(tap: Tap): IO[Water] =
    IO.println(s"Drawing water from ${tap.temp} tap.").as(Water(tap.temp))

  def drawWater(tap: Tap): Stream[IO, Water] =
    Stream.repeatEval(drawOne(tap)).take(3)
  def wash(water: Water): IO[Unit] =
    IO.println(s"Washing with ${water.temp} water.")

  import Temperature.*

  def run = {
    Stream.resource(make(Hot)).flatMap(drawWater).evalMap(wash).compile.drain
  }
}

import fs2.*
import cats.effect.{Trace => _, *}

/* The resource release is executed even on error. */
object Example35 extends IOApp.Simple {
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
  def tooHot(water: Water): IO[Unit] = IO.raiseError(new Error("Too hot!"))

  import Temperature.*

  def run = {
    /* The release is executed even if an error occurs. */
    Stream.resource(make(Hot)).flatMap(drawWater).evalMap(tooHot).compile.drain
  }
}

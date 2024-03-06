import fs2.*
import cats.effect.{Trace => _, *}

/* A resource is used to execute cleanup code regardless of how a program terminates */
object Example30 extends IOApp.Simple {
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

  import Temperature.*

  def run = {
    make(Hot).use(drawOne).void
  }
}

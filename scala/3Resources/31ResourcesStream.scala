import fs2.*
import cats.effect.{Trace => _, *}

/* We lift a resource into a stream with the resource function */
object Example31 extends IOApp.Simple {
  enum Temperature:
    case Hot
    case Cold

  case class Tap(temp: Temperature)
  case class Water(temp: Temperature)
  def make(temp: Temperature): Resource[IO, Tap] =
    Resource.make(IO.println(s"Opening $temp tap.").as(Tap(temp)))(_ =>
      IO.println(s"Closing $temp tap.")
    )
  import Temperature.*

  def run = {
    Stream.resource(make(Hot)).compile.drain
  }
}

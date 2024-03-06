import fs2.*
import cats.effect.{Trace => _, *}
import cats.syntax.all.*

/* The resource release is executed if an error is propagated to it, even if that error is later handled. */
object Example36 extends IOApp.Simple {
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
  def wash(water: Water): IO[Unit] =
    IO.println(s"Washing with ${water.temp} water.")

  import Temperature.*
  val bucket = Water(Cold)

  def run = {
    /* Experiment with the scoping rules of resource and handleError. Notice when the resource is closed. */
    Stream
      .resource(make(Hot))
      .flatMap(drawWater)
      .evalTap(tooHot)
      .handleError(_ => bucket)
      .evalMap(wash)
      .compile
      .drain
  }
}

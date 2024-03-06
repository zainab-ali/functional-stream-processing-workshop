import fs2.*
import cats.effect.{Trace => _, *}
import scala.concurrent.duration.*

/* We can evaluate side-effects as part of a stream by using cats-effect's IO */
object Example70 extends IOApp.Simple {

  def inc(ref: Ref[IO, Int]): IO[Unit] = ref.update(_ + 1)
  def print(value: Int): IO[Unit] = IO.println(s"The value is $value")

  def run: IO[Unit] = {
    for {
      counter <- Ref.of[IO, Int](0)
      _ <- Stream
        .awakeEvery[IO](1.second)
        .evalMap(_ => inc(counter))
        .take(4)
        .compile
        .drain
      value <- counter.get
      _ <- print(value)
    } yield ()
  }
}

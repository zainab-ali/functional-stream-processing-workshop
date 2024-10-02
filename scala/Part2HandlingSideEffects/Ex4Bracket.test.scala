import cats.effect.*
import cats.effect.testkit.*
import munit.*
import fs2.*
import cats.syntax.all.*
import cats.data.Chain
import scala.concurrent.duration.*

class Ex4Bracket extends CatsEffectSuite {

  import Ex4Bracket.*
  case object Err extends Throwable

  // Record the time taken to evaluate a stream.
  // Hint: Call cats.effect.Clock[IO].realTime to get the time
  def timed[A](recorder: Recorder)(in: Stream[IO, A]): Stream[IO, A] = ???

  test("Time a stream") {
    val result = Recorder.run { recorder =>
      Stream
        .sleep[IO](1.second)
        .through(timed(recorder))
    }

    assertIO(TestControl.executeEmbed(result), List(1.second))
  }

  test("Time several streams") {
    val result = Recorder.run { recorder =>
      Stream(1, 2, 3).flatMap { n =>
        Stream.sleep[IO](n.seconds).through(timed(recorder))
      }
    }

    assertIO(
      TestControl.executeEmbed(result),
      List(1.second, 2.seconds, 3.seconds)
    )
  }

  test("Time streams on error") {
    val result = Recorder.run { recorder =>
      (Stream.sleep[IO](1.second) ++ Stream.raiseError[IO](Err))
        .through(timed(recorder))
        .attempt
        .void
    }

    assertIO(
      TestControl.executeEmbed(result),
      List(1.second)
    )
  }
}

object Ex4Bracket {
  final class Recorder(ref: Ref[IO, Chain[FiniteDuration]]) {
    def record(time: FiniteDuration): IO[Unit] = ref.update(_ :+ time)

    private[Recorder] def get: IO[List[FiniteDuration]] = ref.get.map(_.toList)
  }

  object Recorder {
    def apply(): IO[Recorder] =
      Ref.of[IO, Chain[FiniteDuration]](Chain.empty).map(new Recorder(_))

    def run(f: Recorder => Stream[IO, Unit]): IO[List[FiniteDuration]] =
      apply().flatMap { recorder => f(recorder).compile.drain >> recorder.get }
  }
}

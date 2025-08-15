import cats.effect.*
import cats.effect.testkit.*
import munit.*
import fs2.*
import cats.syntax.all.*
import cats.data.Chain
import scala.concurrent.duration.*

class Ex4BracketSolutions extends CatsEffectSuite {

  import Ex4BracketSolutions.*
  case object Err extends Throwable

  // Record the time taken to evaluate a stream.
  // Use the `Recorder.record` function to log the time period.
  // Hint: Call cats.effect.Clock[IO].realTime to get the time
  def timed[A](recorder: Recorder)(in: Stream[IO, A]): Stream[IO, A] = Stream
    .bracket(Clock[IO].realTime)(startTime =>
      Clock[IO].realTime.flatMap(endTime =>
        recorder.record(endTime - startTime)
      )
    )
    .flatMap(_ => in)

  test("Time a stream outputting a single element") {
    val result = Recorder.run { recorder =>
      Stream
        .sleep[IO](1.second)
        .through(timed(recorder))
    }

    assertIO(TestControl.executeEmbed(result), List(1.second))
  }
  test("Time a stream ouputting multiple elements") {
    val result = Recorder.run { recorder =>
      Stream
        .sleep[IO](1.second)
        .repeatN(3)
        .through(timed(recorder))
    }

    assertIO(TestControl.executeEmbed(result), List(3.second))
  }


  test("Time several streams") {
    val result = Recorder.run { recorder =>
      Stream(1, 2, 3).flatMap { n =>
        val inputStream = Stream.sleep[IO](n.seconds)
          inputStream.through(timed(recorder))
      }
    }

    assertIO(
      TestControl.executeEmbed(result),
      List(1.second, 2.seconds, 3.seconds)
    )
  }

  test("Time a stream on error") {
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

object Ex4BracketSolutions {
  final class Recorder(ref: Ref[IO, Chain[FiniteDuration]]) {

    /* Adds a time period to the list of times */
    def record(period: FiniteDuration): IO[Unit] = ref.update(_ :+ period)

    private[Recorder] def get: IO[List[FiniteDuration]] = ref.get.map(_.toList)
  }

  object Recorder {
    def apply(): IO[Recorder] =
      Ref.of[IO, Chain[FiniteDuration]](Chain.empty).map(new Recorder(_))

    /* Evaluates a stream, then returns a list of recorded time periods */
    def run(f: Recorder => Stream[IO, Unit]): IO[List[FiniteDuration]] =
      apply().flatMap { recorder => f(recorder).compile.drain >> recorder.get }
  }
}

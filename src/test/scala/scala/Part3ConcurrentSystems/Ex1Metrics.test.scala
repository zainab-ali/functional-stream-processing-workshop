import munit.*
import fs2.*
import cats.effect.*
import fs2.concurrent.*
import cats.data.*
import cats.effect.testkit.*
import java.util.concurrent.TimeoutException
import scala.concurrent.duration.*

class Ex1Metrics extends CatsEffectSuite {

  import Ex1Metrics.*

  def makeCounter: IO[CounterMetric] = ???

  test("counts number of elements in a stream") {
    val counts = makeCounter.flatMap { counter =>
      counter.counts
        .concurrently(
          Stream
            .range(0, Int.MaxValue)
            .metered[IO](2.second)
            .through(counter.count)
        )
        .interruptAfter(9.seconds)
        .compile
        .toList
    }
    assertIO(TestControl.executeEmbed(counts), List(0, 1, 2, 3, 4))
  }
}

object Ex1Metrics {

  trait CounterMetric {
    def count[A]: Pipe[IO, A, A]
    def counts: Stream[IO, Int]
  }
}

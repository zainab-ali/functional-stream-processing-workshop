import munit.*
import fs2.*
import cats.effect.*
import fs2.concurrent.*
import cats.data.*
import cats.effect.testkit.*
import java.util.concurrent.TimeoutException
import scala.concurrent.duration.*

class Ex1Logger extends CatsEffectSuite {

  import Ex1Logger.*

  /** Use channels to construct a logger.  Messages should be passed through the `writeToDisk` pipe. */
  def makeLogger(writeToDisk: Pipe[IO, String, Nothing]): IO[Logger] = ???

  test("Should log messages from different producers") {
    val messages = Recorder()
      .flatMap { recorder =>
        Stream
          .eval(makeLogger(_.evalMap(recorder.record).drain))
          .flatMap { logger =>
            Stream
              .eval(logger.log("Mao"))
              .merge(Stream.eval(logger.log("Popcorn")))
              .through(logger.writeInBackground)
          }
          .compile
          .drain
          .flatMap { _ => recorder.get }
      }
      .map(_.toSet)
    assertIO(TestControl.executeEmbed(messages), Set("Mao", "Popcorn"))
  }

  test("Should not buffer log messages") {
    val timedOut = Stream
      .eval(makeLogger(_ => Stream.empty))
      .flatMap { logger =>
        Stream
          .eval(logger.log("Mao"))
          .merge(Stream.eval(logger.log("Popcorn")))
          .through(logger.writeInBackground)
      }
      .compile
      .drain
      .timeout(10.seconds)
      .attempt
      .map {
        case Left(_: TimeoutException) => true
        case _                         => false
      }
    assertIO(TestControl.executeEmbed(timedOut), true)
  }
}

object Ex1Logger {
  trait Logger {
    def log(message: String): IO[Unit]
    def writeInBackground[A]: Pipe[IO, A, A]
  }

  final class Recorder(ref: Ref[IO, Chain[String]]) {
    def record(str: String): IO[Unit] = ref.update(_ :+ str)
    def get: IO[List[String]] = ref.get.map(_.toList)
  }

  object Recorder {
    def apply(): IO[Recorder] =
      Ref.of[IO, Chain[String]](Chain.empty).map(new Recorder(_))
  }

}

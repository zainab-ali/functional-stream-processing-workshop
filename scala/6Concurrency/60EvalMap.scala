import fs2.*
import cats.effect.*
import scala.concurrent.duration.*
/* We can evaluate side-effects as part of a stream by using cats-effect's IO */
object Example60 extends IOApp.Simple {

  /* This side-effect sleeps for a given amount of time */
  def eval(time: Int, i: Long): IO[Unit] = IO.println(
    s"Starting to process $i ($time seconds)"
  ) >> IO.sleep(time.seconds) >> (IO.println(s"Processed $i"))

  def run: IO[Unit] = {
    Stream(5, 4, 3, 2, 1).zipWithIndex.evalMap(eval).compile.drain
  }
}

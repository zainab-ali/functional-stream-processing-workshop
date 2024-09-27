import fs2.*
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import scala.util.control.NoStackTrace
import cats.syntax.all.*

object BOOM extends Throwable("BOOM!") with NoStackTrace
val errStream = Stream.raiseError[IO](BOOM)

errStream.compile.drain.unsafeRunSync()

IO.raiseError(BOOM).unsafeRunSync()

def raiseIfTwo(n: Int): IO[Unit] =
  IO.raiseWhen(n == 2)(BOOM)

Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .evalMap(n => IO.println(n))
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .handleError(_ => 42)
  .compile
  .toList
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .handleErrorWith(_ => Stream(42, 43))
  .compile
  .toList
  .unsafeRunSync()

// Retries

def numbers: Stream[IO, Int] = Stream(1, 2, 3)
  .evalTap(raiseIfTwo)
  .handleErrorWith(_ => numbers)

numbers
  .evalMap(x => IO.println(x))
  .take(2)
  .compile
  .drain
  .unsafeRunSync()

import cats.effect.*
import munit.*
import fs2.*
import cats.syntax.all.*

class Ex3Errors extends CatsEffectSuite {

  case object Err extends Throwable

  def eat(name: String): IO[String] =
    if (name === "Popcorn") IO.raiseError(Err) else IO(s"$name eats.")

  def nap(name: String): IO[String] = IO(s"$name naps.")

  val kittens = Stream("Mao", "Popcorn")

  test("Raise an error") {
    val result: Stream[IO, Nothing] = ???
    assertIO(result.compile.drain.attempt, Left(Err))
  }

  test("Mao eats, then Popcorn errors.") {
    val result: Stream[IO, String] = ???
    assertIO(result.compile.toList.attempt, Left(Err)) *>
      assertIO(
        result.attempt.compile.toList,
        List(Right("Mao eats."), Left(Err))
      )
  }

  test("Use attempt: Mao eats, then Popcorn errors.") {
    val result: Stream[IO, Either[Throwable, String]] = ???
    assertIO(result.compile.toList, List(Right("Mao eats."), Left(Err)))
  }
  test("Use handleErrorWith to nap when Popcorn errors.") {
    val result: Stream[IO, String] = ???
    assertIO(
      result.compile.toList,
      List("Mao eats.", "Mao naps.", "Popcorn naps.")
    )
  }
}

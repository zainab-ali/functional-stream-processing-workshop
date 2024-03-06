import cats.effect.*
import munit.*
import fs2.*
import cats.syntax.all.*
import cats.data.*

/** Aim: Build a stream from other streams.
  *
  * * Outcome:
  *   - Be able to combine streams with zip and interleave.
  *   - Understand that there is an order to effects when using non-concurrent
  *     operators.
  */
class Ex4Combining extends CatsEffectSuite {

  final class Recorder(ref: Ref[IO, Chain[String]]) {
    def record(str: String): IO[Unit] = ref.update(_ :+ str)
    def get: IO[List[String]] = ref.get.map(_.toList)
  }

  object Recorder {
    def apply(): IO[Recorder] =
      Ref.of[IO, Chain[String]](Chain.empty).map(new Recorder(_))
  }

  case object Err extends Throwable

  def eat(name: String): IO[String] =
    if (name === "Popcorn") IO.raiseError(Err) else IO(s"$name eats.")

  def naps(name: String): IO[String] = IO(s"$name naps.")

  val kittens = Stream("Mao", "Popcorn")

  val food = Stream("Tuna", "Duck", "Chicken")

  test("zip") {
    val result: Stream[Pure, (String, String)] = ???
    assertEquals(
      result.compile.toList,
      List(("Mao", "Tuna"), ("Popcorn", "Duck"))
    )
  }

  test("interleave") {
    val result: Stream[Pure, String] = ???
    assertEquals(result.compile.toList, List("Mao", "Tuna", "Popcorn", "Duck"))
  }

  test("zip and effects") {
    Recorder().flatMap { recorder =>
      val result: Stream[IO, Nothing] = kittens
        .evalMap(recorder.record)
        .zip(food.evalMap(recorder.record))
        .drain
      val expected: List[String] = ???
      assertIO(result.compile.drain >> recorder.get, expected)
    }
  }

  test("flatMap") {
    val result: Stream[Pure, String] = ???
    assertEquals(
      result.compile.toList,
      List(
        "Mao-Tuna",
        "Mao-Duck",
        "Mao-Chicken",
        "Popcorn-Tuna",
        "Popcorn-Duck",
        "Popcorn-Chicken"
      )
    )
  }
}

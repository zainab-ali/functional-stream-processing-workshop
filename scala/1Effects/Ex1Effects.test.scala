import cats.effect.*
import munit.*
import fs2.*
import cats.data.*

/** Aim: Build a stream with effects using eval, exec and evalMap.
  *
  * * Outcome: Predict the output of a stream built using these primitives.
  *   - Understand that effects aren't evaluated unless we pull on them.
  *   - Understand that the effects are evaluated each time we "run" the stream.
  */
class Ex1Effects extends CatsEffectSuite {
  final class Recorder(ref: Ref[IO, Chain[String]]) {
    def record(str: String): IO[Unit] = ref.update(_ :+ str)
    def get: IO[List[String]] = ref.get.map(_.toList)
  }

  object Recorder {
    def apply(): IO[Recorder] =
      Ref.of[IO, Chain[String]](Chain.empty).map(new Recorder(_))
  }

  def eat(name: String): IO[String] = IO(s"$name eats.")

  def play(recorder: Recorder, name: String): IO[Unit] =
    recorder.record(s"$name plays.")

  val kittens = Stream("Mao", "Popcorn")

  test("Mao eats, then Popcorn eats") {
    val result: Stream[IO, String] = ???
    assertIO(result.compile.toList, List("Mao eats.", "Popcorn eats."))
  }

  test("Mao eats infinitely") {
    val result: Stream[IO, String] = ???
    assertIO(result.take(2).compile.toList, List("Mao eats.", "Mao eats.")) *>
      assertIO(
        result.take(3).compile.toList,
        List("Mao eats.", "Mao eats.", "Mao eats.")
      )
  }

  test("Mao plays once") {
    Recorder().flatMap { recorder =>
      val result: Stream[IO, Nothing] = ???
      assertIO(result.compile.drain *> recorder.get, List("Mao plays."))
    }
  }

  test("Mao plays infinitely") {
    Recorder().flatMap { recorder =>
      val result: Stream[IO, Unit] = ???
      assertIO(
        result.take(2).compile.drain *> recorder.get,
        List("Mao plays.", "Mao plays.")
      ) *>
        assertIO(
          result.head.compile.drain *> recorder.get,
          List("Mao plays.", "Mao plays.", "Mao plays.")
        )
    }
  }

}

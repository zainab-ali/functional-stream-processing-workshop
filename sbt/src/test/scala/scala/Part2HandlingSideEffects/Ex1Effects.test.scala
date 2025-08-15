import cats.effect.*
import munit.*
import fs2.*
import cats.data.*

class Ex1Effects extends CatsEffectSuite {
  import Ex1Effects.*
  def eat(name: String): IO[String] = IO(s"$name eats.")

  def play(testHelper: TestHelper, name: String): IO[Unit] =
    testHelper.record(s"$name plays.")

  val kittens = Stream("Mao", "Popcorn")

  test("Mao eats, then Popcorn eats") {
    // Use the kittens stream and the eat function to solve this problem.
    val result: Stream[IO, String] = ???
    assertIO(result.compile.toList, List("Mao eats.", "Popcorn eats."))
  }

  test("Mao eats infinitely") {
    // Use the kittens stream and the eat function to solve this problem.
    val result: Stream[IO, String] = ???
    assertIO(result.take(2).compile.toList, List("Mao eats.", "Mao eats.")) *>
      assertIO(
        result.take(3).compile.toList,
        List("Mao eats.", "Mao eats.", "Mao eats.")
      )
  }

  test("Mao plays once") {
    // Use the kittens stream and the play function to solve this problem.
    TestHelper().flatMap { testHelper =>
      val result: Stream[IO, Unit] = ???
      assertIO(result.compile.drain *> testHelper.get, List("Mao plays."))
    }
  }

  test("Mao plays infinitely") {
    TestHelper().flatMap { testHelper =>
      // Use the kittens stream and the play function to solve this problem.
      val result: Stream[IO, Unit] = ???
      assertIO(
        result.take(2).compile.drain *> testHelper.get,
        List("Mao plays.", "Mao plays.")
      ) *>
        assertIO(
          result.head.compile.drain *> testHelper.get,
          List("Mao plays.", "Mao plays.", "Mao plays.")
        )
    }
  }
}

object Ex1Effects {
  final class TestHelper(ref: Ref[IO, Chain[String]]) {
    def record(str: String): IO[Unit] = ref.update(_ :+ str)
    def get: IO[List[String]] = ref.get.map(_.toList)
  }

  object TestHelper {
    def apply(): IO[TestHelper] =
      Ref.of[IO, Chain[String]](Chain.empty).map(new TestHelper(_))
  }
}

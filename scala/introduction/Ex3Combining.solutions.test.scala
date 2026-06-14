import cats.effect.*
import munit.*
import fs2.*
import cats.syntax.all.*
import cats.data.*

class Ex3CombiningSolutions extends CatsEffectSuite {

  val kittens = Stream("Mao", "Popcorn")

  val food = Stream("Tuna", "Duck", "Chicken")

  test("kittens and then food") {
    val result: Stream[Pure, String] = kittens ++ food
    assertEquals(
      result.compile.toList,
      List("Mao", "Popcorn", "Tuna", "Duck", "Chicken")
    )
  }

  test("kittens tupled with food") {
    val result: Stream[Pure, (String, String)] = kittens.zip(food)
    assertEquals(
      result.compile.toList,
      List(("Mao", "Tuna"), ("Popcorn", "Duck"))
    )
  }

  test("a kitten, some food, another kitten") {
    val result: Stream[Pure, String] = kittens.interleave(food)
    assertEquals(result.compile.toList, List("Mao", "Tuna", "Popcorn", "Duck"))
  }

  test("kittens paired with all foods") {
    val result: Stream[Pure, String] = kittens.flatMap { kitten =>
      food.map { foodItem => s"$kitten-$foodItem" }
    }
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

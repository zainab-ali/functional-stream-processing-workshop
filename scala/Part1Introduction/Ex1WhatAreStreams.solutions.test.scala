import munit.*
import fs2.*

class Ex1WhatAreStreamsSolutions extends CatsEffectSuite {

  val numbers: Stream[Pure, Int] = Stream.range(0, Int.MaxValue)

  test("get the first two numbers") {
    val result: List[Int] = numbers.take(2).toList
    assertEquals(result, List(0, 1))
  }

  test("get the seventh to the ninth number") {
    val result: List[Int] = numbers.drop(6).take(3).toList
    assertEquals(result, List(6, 7, 8))
  }

  test("get odd numbers before the number 6") {
    val result: List[Int] = numbers
      .filter(n => n % 2 == 1)
      .takeWhile(_ < 6)
      .toList
    assertEquals(result, List(1, 3, 5))
  }

  test("get the 1000th number") {
    val result: Int = numbers.drop(999).head.compile.last.get
    assertEquals(result, 999)
  }

  test("get the sum of the first ten numbers") {
    // `foldMonoid` sums all the numbers in a stream.
    // Note that there are many other ways to solve this exercise.
    val result = numbers.take(10).compile.foldMonoid
    assertEquals(result, 45)
  }
}

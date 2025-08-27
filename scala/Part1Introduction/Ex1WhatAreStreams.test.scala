import munit.*
import fs2.*

class Ex1WhatAreStreams extends CatsEffectSuite {

  val numbers: Stream[Pure, Int] = Stream.range(0, Int.MaxValue)

  test("get the first two numbers") {
    val result: List[Int] = ???
    assertEquals(result, List(0, 1))
  }

  test("get the seventh to the ninth number") {
    val result: List[Int] = ???
    assertEquals(result, List(6, 7, 8))
  }

  test("get odd numbers less than six") {
    val result: List[Int] = ???
    assertEquals(result, List(1, 3, 5))
  }

  test("get the 1000th number") {
    val result: Int = ???
    assertEquals(result, 999)
  }

  test("get the sum of the first 10 numbers") {
    val result = ???
    assertEquals(result, 45)
  }
}

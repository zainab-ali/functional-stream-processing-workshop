import cats.effect.*
import cats.effect.testkit.*
import munit.*
import fs2.*
import cats.syntax.all.*
import cats.data.*
import scala.concurrent.duration.*

class Ex4FanOutAndIn extends CatsEffectSuite {

  def generateTestBook(title: String): Stream[IO, String] = {
    val sentenceData: Stream[Pure, String] =
      Stream("This is a test sentence").flatMap(sentence =>
        Stream.emits(sentence.split(" "))
      )
    Stream
      .sleep[IO](1.second)
      .flatMap(_ => sentenceData.repeatN(4L))
      .map(word => s"$title: $word")
  }

  def countWords(book: Stream[IO, String]): Stream[IO, Long] =
    book.map(_ => 1L).foldMonoid

  def countWordsInBook(title: String): Stream[IO, Long] = countWords(
    generateTestBook(title)
  )

  // Use the `countWordsInBook` function.
  def countWordsInBooksWithFanOutAndIn(
      title: Stream[IO, String]
  ): Stream[IO, Long] = ???

  // Use the `countWordsInBook` function.
  def countWordsInBooksSequentially(
      books: Stream[IO, String]
  ): Stream[IO, Long] = ???

  test("fan-out fan-in") {
    val books = Stream("little-dorrit", "hard-times")
    val result: IO[(FiniteDuration, Option[Long])] =
      countWordsInBooksWithFanOutAndIn(books).compile.last.timed

    assertIO(TestControl.executeEmbed(result), (1.seconds, Some(40L)))
  }

  test("sequential") {
    val books = Stream("little-dorrit", "hard-times")
    val result: IO[(FiniteDuration, Option[Long])] =
      countWordsInBooksSequentially(books).compile.last.timed

    assertIO(TestControl.executeEmbed(result), (2.seconds, Some(40L)))
  }

}

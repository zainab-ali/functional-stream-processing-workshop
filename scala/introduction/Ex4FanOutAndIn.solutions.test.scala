import cats.effect.*
import cats.effect.testkit.*
import munit.*
import fs2.*
import cats.syntax.all.*
import cats.data.*
import scala.concurrent.duration.*

class Ex4FanOutAndInSolutions extends CatsEffectSuite {

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
      titles: Stream[IO, String]
  ): Stream[IO, Long] =
    titles.map(title => countWordsInBook(title)).parJoinUnbounded.foldMonoid

  // Use the `countWordsInBook` function.
  def countWordsInBooksSequentially(
      titles: Stream[IO, String]
  ): Stream[IO, Long] = titles.flatMap { title =>
    countWordsInBook(title)
  }.foldMonoid

  test("fan-out fan-in") {
    val titles = Stream("little-dorrit", "hard-times")
    val result: IO[(FiniteDuration, Option[Long])] =
      countWordsInBooksWithFanOutAndIn(titles).compile.last.timed

    assertIO(TestControl.executeEmbed(result), (1.seconds, Some(40L)))
  }

  test("sequential") {
    val titles = Stream("little-dorrit", "hard-times")
    val result: IO[(FiniteDuration, Option[Long])] =
      countWordsInBooksSequentially(titles).compile.last.timed

    assertIO(TestControl.executeEmbed(result), (2.seconds, Some(40L)))
  }

}

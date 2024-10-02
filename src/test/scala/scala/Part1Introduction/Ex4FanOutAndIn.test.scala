import cats.effect.*
import cats.effect.testkit.*
import munit.*
import fs2.*
import cats.syntax.all.*
import cats.data.*
import scala.concurrent.duration.*

class Ex4FanOutAndIn extends CatsEffectSuite {

  val sentenceData: Stream[Pure, String] =
    Stream("This is a test sentence").flatMap(sentence =>
      Stream.emits(sentence.split(" "))
    )
  def bookData(title: String): Stream[IO, String] =
    Stream
      .sleep[IO](1.second)
      .flatMap(_ => sentenceData.repeatN(4L))
      .map(word => s"book$title: $word")

  def countWordsInBooksWithParJoinUnbounded(
      books: Stream[IO, Stream[IO, String]]
  ): Stream[IO, Long] = ???

  def countWordsInBooksSequentially(
      books: Stream[IO, Stream[IO, String]]
  ): Stream[IO, Long] = ???

  def countWords(book: Stream[IO, String]): Stream[IO, Long] =
    book.map(_ => 1L).fold1(_ + _)

  test("fan-out fan-in") {
    val books = Stream(bookData("little-dorrit"), bookData("hard-times"))
    val result: IO[(FiniteDuration, Option[Long])] =
      countWordsInBooksWithParJoinUnbounded(books).compile.last.timed

    assertIO(TestControl.executeEmbed(result), (1.seconds, Some(40L)))
  }

  test("sequential") {
    val books = Stream(bookData("little-dorrit"), bookData("hard-times"))
    val result: IO[(FiniteDuration, Option[Long])] =
      countWordsInBooksSequentially(books).compile.last.timed

    assertIO(TestControl.executeEmbed(result), (2.seconds, Some(40L)))
  }

}

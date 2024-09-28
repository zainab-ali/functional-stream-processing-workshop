import fs2.*

import fs2.*
import cats.effect.IO

def fanOutAndIn[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, B] = input.map(fanFunction).parJoinUnbounded

val sentenceData: Stream[Pure, String] =
  Stream("This is a test sentence").flatMap(sentence =>
    Stream.emits(sentence.split(" "))
  )

sentenceData.compile.toList

import cats.effect.*
import scala.concurrent.duration.*
def bookData(title: String): Stream[IO, String] =
  Stream
    .sleep[IO](1.second)
    .flatMap(_ => sentenceData.repeatN(4L))
    .map(word => s"book$title: $word")

import cats.effect.unsafe.implicits.global
val firstSixWordsOfLittleDorritIO =
  bookData("little-dorrit").take(6).compile.toList
val (firstSixWordsTime, firstSixWordsOfLittleDorrit) =
  firstSixWordsOfLittleDorritIO.timed.unsafeRunSync()

def countWords(book: Stream[IO, String]): Stream[IO, Long] =
  book.map(_ => 1L).fold1(_ + _)

def countWordsInBooks(books: Stream[IO, Stream[IO, String]]): Stream[IO, Long] =
  books.map(book => countWords(book)).parJoinUnbounded.fold1(_ + _)

val (timeToCompute, totalCount) = countWordsInBooks(
  Stream(bookData("little-dorrit"), bookData("hard-times"))
).compile.last.timed.unsafeRunSync()
timeToCompute.toMillis
println(s"Counting words with fan-in took ${timeToCompute.toMillis}ms")

def countWordsInBooksSequentially(
    books: Stream[IO, Stream[IO, String]]
): Stream[IO, Long] =
  books
    .flatMap { book => countWords(book) }
    .fold1(_ + _)

val (timeToComputeSequentially, totalCountSequential) =
  countWordsInBooksSequentially(
    Stream(bookData("little-dorrit"), bookData("hard-times"))
  ).compile.last.timed.unsafeRunSync()

println(
  s"Counting words sequentially took ${timeToComputeSequentially.toMillis}ms"
)

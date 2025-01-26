import fs2.*

import cats.effect.*
import scala.concurrent.duration.*

def fanOutAndIn[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, B] = input.map(fanFunction).parJoinUnbounded

val sentenceData: Stream[Pure, String] = Stream("This", "is", "a", "test", "sentence")
sentenceData.compile.toList

import cats.effect.unsafe.implicits.global
Stream.sleep[IO](1.second).compile.last.unsafeRunSync()

def bookData(title: String): Stream[IO, String] =
  Stream.sleep[IO](1.second)
    .flatMap(_ => sentenceData.repeatN(4L))
    .map(word => s"$title: $word")

val firstSixWordsOfLittleDorritIO =
  bookData("little-dorrit").take(6).compile.toList

val (firstSixWordsTime, firstSixWordsOfLittleDorrit) =
  firstSixWordsOfLittleDorritIO.timed.unsafeRunSync()

def countWords(book: Stream[IO, String]): Stream[IO, Long] =
  book.map(_ => 1L).fold1(_ + _)

def countWordsInBooks(books: Stream[IO, Stream[IO, String]]): Stream[IO, Long] =
  fanOutAndIn(books, book => countWords(book)).fold1(_ + _)

val (timeToCompute, totalCount) = countWordsInBooks(
  Stream(bookData("little-dorrit"), bookData("hard-times"))
).compile.last.timed.unsafeRunSync()

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

println(s"Fan out/in: $timeToCompute")
println(s"Sequential: $timeToComputeSequentially")

import fs2.io.file.*
def realBookData(title: String): Stream[IO, String] =
  Files[IO].readUtf8Lines(Path("data") / s"$title.txt")
  .flatMap(line => Stream.emits(line.split("")))

val (timeToComputeReal, totalCountReal) = countWordsInBooks(
  Stream(realBookData("little-dorrit"), realBookData("hard-times"))
).compile.last.timed.unsafeRunSync()

val (timeToComputeSequentiallyReal, totalCountSequentialReal) = countWordsInBooksSequentially(
  Stream(realBookData("little-dorrit"), realBookData("hard-times"))
).compile.last.timed.unsafeRunSync()

println(s"Real fan out/in: $timeToComputeReal")
println(s"Real sequential: $timeToComputeSequentiallyReal")


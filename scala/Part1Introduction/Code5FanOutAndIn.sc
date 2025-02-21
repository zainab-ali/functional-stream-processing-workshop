import fs2.*

import cats.effect.*
import scala.concurrent.duration.*

def fanIn[A](input: Stream[IO, Stream[IO, A]]): Stream[IO, A] =
  input.parJoinUnbounded

def fanOut[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, Stream[IO, B]] = input.map(fanFunction)

def fanOutAndIn[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, B] = fanIn(fanOut(input, fanFunction))

val sentenceData: Stream[Pure, String] =
  Stream("This", "is", "a", "test", "sentence")
sentenceData.compile.toList

import cats.effect.unsafe.implicits.global
Stream.sleep[IO](1.second).compile.last.unsafeRunSync()

def generateTestBook(title: String): Stream[IO, String] =
  Stream
    .sleep[IO](1.second)
    .flatMap(_ => sentenceData.repeatN(4L))
    .map(word => s"$title: $word")

val firstSixWordsOfLittleDorritIO =
  generateTestBook("little-dorrit").take(6).compile.toList

val (firstSixWordsTime, firstSixWordsOfLittleDorrit) =
  firstSixWordsOfLittleDorritIO.timed.unsafeRunSync()

def countWords(book: Stream[IO, String]): Stream[IO, Long] =
  book.map(_ => 1L).foldMonoid

def countWordsInBook(title: String): Stream[IO, Long] = countWords(
  generateTestBook(title)
)

def countWordsInBooks(titles: Stream[IO, String]): Stream[IO, Long] =
  fanOutAndIn(titles, title => countWordsInBook(title)).foldMonoid

val (timeToCompute, _) = countWordsInBooks(
  Stream("little-dorrit", "hard-times")
).compile.last.timed.unsafeRunSync()

def countWordsInBooksSequentially(
    titles: Stream[IO, String]
): Stream[IO, Long] =
  titles
    .flatMap(title => countWordsInBook(title))
    .foldMonoid

val (timeToComputeSequentially, _) =
  countWordsInBooksSequentially(
    Stream("little-dorrit", "hard-times")
  ).compile.last.timed.unsafeRunSync()

println(s"Fan out/in: $timeToCompute")
println(s"Sequential: $timeToComputeSequentially")

import fs2.io.file.*

def realBookData(title: String): Stream[IO, String] =
  Files[IO]
    .readUtf8Lines(Path("data") / s"$title.txt")
    .flatMap(line => Stream.emits(line.split("")))

def countWordsInRealBook(title: String): Stream[IO, Long] = countWords(
  realBookData(title)
)

def countWordsInRealBooks(titles: Stream[IO, String]): Stream[IO, Long] =
  fanOutAndIn(titles, title => countWordsInRealBook(title)).foldMonoid

def countWordsInRealBooksSequentially(
    titles: Stream[IO, String]
): Stream[IO, Long] =
  titles
    .flatMap(title => countWordsInRealBook(title))
    .foldMonoid

val (timeToComputeReal, _) = countWordsInRealBooks(
  Stream("little-dorrit", "hard-times")
).compile.last.timed.unsafeRunSync()

val (timeToComputeSequentiallyReal, _) = countWordsInRealBooksSequentially(
  Stream("little-dorrit", "hard-times")
).compile.last.timed.unsafeRunSync()

println(s"Real fan out/in: $timeToComputeReal")
println(s"Real sequential: $timeToComputeSequentiallyReal")

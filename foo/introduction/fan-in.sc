import fs2.*
import cats.effect.IO

def fanIn[A](input: Stream[IO, Stream[IO, A]]): Stream[IO, A] = input.parJoinUnbounded

case class ProcessCmd(cmd: String)
def processes: Stream[IO, ProcessCmd] = ???
def runAndGetLogs(processName: ProcessCmd): Stream[IO, String] = ???

def processLogs: Stream[IO, Stream[IO, String]] = processes.map(runAndGetLogs)

def fanOut[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, Stream[IO, B]] = input.map(fanFunction)

import fs2.*
import cats.effect.IO

def fanOutAndIn[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, B] = fanIn(fanOut(input, fanFunction))

import fs2.*
def generateTestTitles: Stream[Pure, String] = Stream("little-dorrit", "hard-times")

import fs2.*
val bookData: Stream[Pure, String] = Stream("This", "is", "a", "test", "sentence").repeatN(4L)

bookData.take(6).compile.toList

import cats.effect.IO
import scala.concurrent.duration.* // Needed to use the `millis` syntax
val sleepStream = Stream.sleep[IO](10.millis)

val sleepProgram = sleepStream.compile.last

import cats.effect.unsafe.implicits.global
sleepProgram.unsafeRunSync()

import cats.effect.*
def generateTestBook(title: String): Stream[IO, String] =
  Stream
    .sleep[IO](10.millis)
    .flatMap(_ => bookData)
    .map(word => s"$title: $word")

generateTestBook("little-dorrit").take(6)
  .compile.toList
  .timed.unsafeRunSync()

def countWords(book: Stream[IO, String]): Stream[IO, Long] =
  book.map(_ => 1L).foldMonoid

def countWordsInBook(title: String): Stream[IO, Long] = countWords(generateTestBook(title))

def countWordsInBooks(titles: Stream[IO, String]): Stream[IO, Long] =
  fanOutAndIn(titles, title => countWordsInBook(title)).foldMonoid

countWordsInBooks(
  generateTestTitles
).compile.last.timed.unsafeRunSync()

def countWordsInBooksSequentially(
    titles: Stream[IO, String]
): Stream[IO, Long] =
  titles
    .flatMap(title => countWordsInBook(title))
    .foldMonoid

countWordsInBooksSequentially(
    generateTestTitles
).compile.last.timed.unsafeRunSync()

import fs2.io.file.*

def readRealBook(title: String): Stream[IO, String] =
  Files[IO].readUtf8Lines(Path("data") / s"$title.txt")
  .flatMap(line => Stream.emits(line.split(" ")))
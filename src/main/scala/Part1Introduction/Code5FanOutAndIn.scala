package Part1Introduction

import cats.effect.unsafe.implicits.global
final class Code5FanOutAndIn$_ {
def args = Code5FanOutAndIn_sc.args$
def scriptPath = """Part1Introduction/Code5FanOutAndIn.sc"""
/*<script>*/
import fs2.*

import cats.effect.*
import scala.concurrent.duration.*

def fanOutAndIn[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, B] = input.map(fanFunction).parJoinUnbounded

val sentenceData: Stream[Pure, String] =
  Stream("This is a test sentence").flatMap(sentence =>
    Stream.emits(sentence.split(" "))
  )

sentenceData.compile.toList

Stream.sleep[IO](1.second).compile.last.unsafeRunSync()

def bookData(title: String): Stream[IO, String] =
  Stream.sleep[IO](1.second)
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
  fanOutAndIn(books, book => countWords(book)).fold1(_ + _)

val (timeToCompute, totalCount) = countWordsInBooks(
  Stream(bookData("little-dorrit"), bookData("hard-times"))
).compile.last.timed.unsafeRunSync()
timeToCompute.toMillis

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

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code5FanOutAndIn_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code5FanOutAndIn$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code5FanOutAndIn_sc.script as `Code5FanOutAndIn`


import fs2.*

val sentenceData: Stream[Pure, String] =
  Stream("This is a test sentence").flatMap(sentence =>
    Stream.emits(sentence.split(" "))
  )

sentenceData.compile.toList

import cats.effect.*
import scala.concurrent.duration.*
def chapterData(n: Int): Stream[IO, String] =
  Stream
    .sleep[IO](1.second)
    .flatMap(_ => sentenceData.repeatN(4L))
    .map(word => s"ch$n: $word")

val firstSixWordsOfChapterTwo = chapterData(2).take(6).compile.toList

import cats.effect.unsafe.implicits.global
firstSixWordsOfChapterTwo.unsafeRunSync()

def countWords(in: Stream[IO, String]): Stream[IO, Int] =
  in.map(_ => 1).fold1(_ + _)

def countWordsSequentially(
    chapterOne: Stream[IO, String],
    chapterTwo: Stream[IO, String]
): IO[Int] = (countWords(chapterOne) ++ countWords(chapterTwo))
  .fold1(_ + _)
  .compile
  .last
  .map(_.getOrElse(0))

val (timeToCompute, totalCount) =
  countWordsSequentially(chapterData(1), chapterData(2)).timed.unsafeRunSync()
println(s"Counting words took ${timeToCompute.toMillis}ms")

def countWordsFanIn(
    chapterOne: Stream[IO, String],
    chapterTwo: Stream[IO, String]
): IO[Int] = countWords(chapterOne)
  .merge(countWords(chapterTwo))
  .fold1(_ + _)
  .compile
  .last
  .map(_.getOrElse(0))

val (timeToComputeFanIn, totalCountFanIn) =
  countWordsFanIn(chapterData(1), chapterData(2)).timed.unsafeRunSync()
println(s"Counting words with fan-in took ${timeToComputeFanIn.toMillis}ms")

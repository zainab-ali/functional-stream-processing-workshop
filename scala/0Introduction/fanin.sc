import fs2.*
import cats.effect.*

import scala.concurrent.duration.*
import cats.effect.unsafe.implicits.global

def chapterTestData(n: Int) =
  Stream("This", "is", "chapter", n.toString).repeatN(4L)

chapterTestData(3).take(5).compile.toList

val bookTestData = Stream.range(0, 3).map(chapterTestData)

def countWords: Pipe[IO, String, Int] = in =>
  Stream.sleep[IO](1.second).flatMap(_ => in.map(_ => 1).fold1(_ + _))

def countWordsInBook(book: Stream[Pure, Stream[IO, String]]): IO[Int] =
  book
    .flatMap(chapter => chapter.through(countWords))
    .fold1(_ + _)
    .compile
    .last
    .map(_.getOrElse(0))

val (countWordsTime, _) = countWordsInBook(bookTestData).timed.unsafeRunSync()
println(s"Counting words took ${countWordsTime.toSeconds} seconds")

def countWordsInBookAndFanIn(book: Stream[IO, Stream[IO, String]]): IO[Int] =
  book
    .map(_.through(countWords))
    .parJoinUnbounded
    .fold1(_ + _)
    .compile
    .last
    .map(_.getOrElse(0))

val (countWordsFanInTime, _) =
  countWordsInBookAndFanIn(bookTestData).timed.unsafeRunSync()
println(
  s"Counting words with a fan-in took ${countWordsFanInTime.toSeconds} seconds"
)

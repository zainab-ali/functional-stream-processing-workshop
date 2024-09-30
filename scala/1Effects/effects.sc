import fs2.io.file.Path
import fs2.*

import fs2.io.file.Files
import cats.effect.IO
import cats.effect.unsafe.implicits.global

def countWords(book: Stream[IO, String]): Stream[IO, Long] =
  book.map(_ => 1L).fold1(_ + _)

def countWordsInBooks(books: Stream[IO, Stream[IO, String]]): Stream[IO, Long] =
  books.map(book => countWords(book)).parJoinUnbounded.fold1(_ + _)

def readBook(path: Path): Stream[IO, String] = Files[IO]
  .readUtf8(path)
  .flatMap(str => Stream.emits(str.split(" ")))
  .map(_.trim)
  .filter(_.nonEmpty)

val books = Files[IO].list(Path("data")).debug()
val concurrentProgram = books
  .map(path => countWords(readBook(path)))
  .parJoinUnbounded
  .fold1(_ + _)
  .compile
  .last
val sequentialProgram =
  books.flatMap(path => countWords(readBook(path))).fold1(_ + _).compile.last

val (concTime, _) = concurrentProgram.timed.unsafeRunSync()
println(s"Concurrent ${concTime.toMillis}")
val (seqTime, _) = sequentialProgram.timed.unsafeRunSync()
println(s"Sequential ${seqTime.toMillis}")

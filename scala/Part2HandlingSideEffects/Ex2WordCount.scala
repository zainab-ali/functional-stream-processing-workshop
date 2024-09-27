import fs2.*
import cats.effect.{Trace => _, *}
import fs2.io.file.Path

// We should extend IOApp.Simple when using cats-effect IO
object Ex2WordCount extends IOApp.Simple {

  def countWords(book: Stream[IO, String]): Stream[IO, Long] =
    book.map(_ => 1L).fold1(_ + _)

  def countWordsInBooks(
      books: Stream[IO, Stream[IO, String]]
  ): Stream[IO, Long] =
    books.map(book => countWords(book)).parJoinUnbounded.fold1(_ + _)

  // Use the files API to read UTF8 strings and split them into words
  def readBook(path: Path): Stream[IO, String] = ???

  // Use the files API to list the contents of the data directory
  def books: Stream[IO, Path] = ???

  def run: IO[Unit] = {
    countWordsInBooks(books.map(readBook)).compile.last.flatMap(result =>
      IO.println(s"The result was $result")
    )
  }
}

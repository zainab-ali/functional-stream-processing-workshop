import cats.effect.*
import fs2.*
import fs2.io.file.Path
import fs2.io.file.Files

// Counts the total number of words in all books in the `data` directory
object Ex2WordCountSolutions extends IOApp.Simple {

  val whitespaceRegex: String = "\\s"

  // Use the files API to read UTF8 strings and split them into words. Use the whitespaceRegex to split strings.
  def readBook(title: Path): Stream[IO, String] =
    Files[IO].readUtf8Lines(title).flatMap { line =>
      Stream.emits(line.split("\\s"))
    }

  def countWords(book: Stream[IO, String]): Stream[IO, Long] =
    book.map(_ => 1L).foldMonoid

  def countWordsInBook(title: Path): Stream[IO, Long] = countWords(
    readBook(title)
  )

  def countWordsInBooks(
      titles: Stream[IO, Path]
  ): Stream[IO, Long] =
    titles
      .map(title => countWordsInBook(title))
      .parJoinUnbounded
      .foldMonoid

  // Use the files API to list the contents of the data directory
  def titles: Stream[IO, Path] = Files[IO].list(Path("data"))

  def run: IO[Unit] = {
    countWordsInBooks(titles).compile.last.flatMap(result =>
      IO.println(s"The total word count was $result")
    )
  }
}

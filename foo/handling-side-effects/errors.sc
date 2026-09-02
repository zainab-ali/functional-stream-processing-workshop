import fs2._
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.all.*

import scala.util.control.NoStackTrace
object BOOM extends Throwable("BOOM!") with NoStackTrace

val errStream = Stream.raiseError[IO](BOOM)

errStream.compile.drain.unsafeRunSync()

IO.raiseError(BOOM).unsafeRunSync()

Stream.unit
  .evalMap(_ => IO.raiseError(BOOM))
  .compile
  .drain.unsafeRunSync()

case class Book(title: String, author: String, wordCount: Int)

def totalWordCount(books: Stream[IO, Book]): IO[Int] =
  books.map(_.wordCount).compile.foldMonoid

val books = Stream(
  Book("Emma", "Austen", 78500),
  Book("Little Dorrit", "Dickens", -2),
  Book("Hard Times", "Dickens", 110000)
)

case class CountIsNegative(book: Book)
   extends Throwable(s"${book.title} has ${book.wordCount} words")
   with NoStackTrace

def validateBook(book: Book): IO[Book] =
  IO.raiseWhen(book.wordCount < 0)(CountIsNegative(book)).as(book)

def totalWordCount(books: Stream[IO, Book]): IO[Int] =
  books
    .evalMap(validateBook)
    .map(_.wordCount)
    .compile
    .foldMonoid

totalWordCount(books).unsafeRunSync()

case class MisfiledBook(book: Book)
   extends Throwable(s"${book.title} has been misfiled")
   with NoStackTrace

def validateBook(book: Book): IO[Book] =
  IO.raiseWhen(book.author != "Dickens")(MisfiledBook(book))
    .as(book)

val emma = Book("Emma", "Austen", 78500)
validateBook(emma).unsafeRunSync()

validateBook(emma).attempt: IO[Either[Throwable, Book]]

validateBook(emma).attempt.map(_.toOption): IO[Option[Book]]

validateBook(emma)
  .attempt.map(_.toOption).unsafeRunSync()

val totalCount = books
  .evalMapFilter(book => validateBook(book).attempt.map(_.toOption))
  .map(_.wordCount)
  .compile
  .foldMonoid.unsafeRunSync()

def validateBook(book: Book): IO[Book] =
  IO.raiseWhen(book.author != "Dickens")(MisfiledBook(book))
    .flatMap(_ => IO.raiseWhen(book.wordCount < 0)(CountIsNegative(book)))
    .as(book)

validateBook(emma)
  .attemptNarrow[MisfiledBook]
  .map(_.toOption)

validateBook(Book("Hard Times", "Dickens", 110000))
  .attemptNarrow[MisfiledBook]
  .map(_.toOption).unsafeRunSync()

validateBook(emma)
  .attemptNarrow[MisfiledBook]
  .map(_.toOption).unsafeRunSync()

validateBook(Book("Little Dorrit", "Dickens", -2))
  .attemptNarrow[MisfiledBook]
  .map(_.toOption).unsafeRunSync()

def totalWordCount(books: Stream[IO, Book]): IO[Int] =
  books
    .evalMapFilter(book =>
      validateBook(book).attemptNarrow[MisfiledBook].map(_.toOption)
    )
    .map(_.wordCount)
    .compile
    .foldMonoid

totalWordCount(books).unsafeRunSync()

import cats.effect.std.Random

val randomInstance = Random.scalaUtilRandomSeedInt[IO](1).unsafeRunSync()

case object BadBook extends Throwable with NoStackTrace

def lookUpBook(name: String): IO[Book] = randomInstance.nextBoolean.flatMap { result =>
  randomInstance.betweenInt(100, Int.MaxValue).flatMap { count =>
      IO.raiseWhen(result)(BadBook).as(Book(name, "Dickens", count))
  }
}

def retryLookUpBook(title: String): IO[Book] = Stream
  .eval(lookUpBook(title).attempt.map(_.toOption))
  .repeat
  .evalTap(_ => IO.println(s"Called API for $title."))
  .collect { case Some(book) => book }
  .head
  .compile
  .lastOrError

retryLookUpBook("Little Dorrit").unsafeRunSync()

import fs2.io.file.*
def getContentsFromDisk(title: String): Stream[IO, String] =
  Files[IO].readUtf8Lines(Path("data") / s"$title.txt").flatMap { line =>
    Stream.emits(line.split("\\s"))
  }

getContentsFromDisk("Bleak House").compile.count.unsafeRunSync()

def getContentsFromApi(title: String): Stream[IO, String] =
  Stream("In", "Chancery", "London")

def getContents(title: String): Stream[IO, String] =
  getContentsFromDisk(title).handleErrorWith { _ =>
    getContentsFromApi(title)
  }

getContents("Bleak House").take(3).compile.count.unsafeRunSync()
import fs2.*
import cats.effect.*
import aquascape.*
import cats.syntax.all.*

object Fig2Errors extends WorkshopAquascapeApp {

  case class Book(title: String, author: String, wordCount: Int)

  given cats.Show[Book] = _.title
  val books = Stream(
    Book("Emma", "Austen", 78500),
    Book("Little Dorrit", "Dickens", -2),
    Book("Hard Times", "Dickens", 110000)
  )

  def validateBook(book: Book): IO[Book] =
    IO.raiseWhen(book.wordCount < 0)(CountIsNegative(book)).as(book)

  case class CountIsNegative(book: Book)
      extends Throwable(s"${book.title} has ${book.wordCount} words")

  def stream(using Scape[IO]) = {
    books
      .stage("books")
      .evalMap(validateBook)
      .stage("evalMap(validateBook)")
      .map(_.wordCount)
      .compile
      .foldMonoid
      .compileStage("compile.foldMonoid")
      .attempt
      .void
  }
}

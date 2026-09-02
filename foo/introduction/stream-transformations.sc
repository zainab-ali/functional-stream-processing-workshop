import fs2.*
import cats.effect.IO

Stream(1, 2, 3)      // input stream
  .take(2)           // take2
  .map(_ + 1)        // add1
  .intersperse(3)    // intersperse3
  .compile.count

def take2(s: Stream[Pure, Int]): Stream[Pure, Int] = s.take(2)
def add1(s: Stream[Pure, Int]): Stream[Pure, Int] = s.map(_ + 1)
def intersperse3(s: Stream[Pure, Int]): Stream[Pure, Int] = s.intersperse(3)

intersperse3(add1(take2(Stream(1, 2, 3)))).compile.count

// type Pipe[F[_], I, O] = Stream[F, I] => Stream[F, O]

// def take2(s: Stream[Pure, Int]): Stream[Pure, Int] = s.take(2)

// val take2: Pipe[Pure, Int, Int] = _.take(2)

Stream(1, 2, 3)
  .through(take2)

take2(Stream(1, 2, 3))

Stream(1, 2, 3)
  .through(take2)
  .through(add1)
  .through(intersperse3)
  .compile.count

def countWordsInBook(title: String): Stream[IO, Long] = ???

def fanOutAndIn[A, B](
    input: Stream[IO, A],
    fanFunction: A => Stream[IO, B]
): Stream[IO, B] = ???
def generateTestTitles: Stream[IO, String] = ???

// def countWordsInBooks(titles: Stream[IO, String]): Stream[IO, Long] =
//   fanOutAndIn(titles, title => countWordsInBook(title)).foldMonoid

val countWordsInBooks: Pipe[IO, String, Long] = titles =>
  titles
  .through(fanOutAndIn(_, title => countWordsInBook(title)))
  .foldMonoid

// generateTestTitles
//   .through(countWordsInBooks)
//   .compile.last.timed
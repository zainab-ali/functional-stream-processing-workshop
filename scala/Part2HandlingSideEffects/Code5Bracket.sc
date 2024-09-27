import fs2.*
import cats.effect.IO
import cats.effect.unsafe.implicits.*

Stream
  .bracket(IO.println("Acquiring"))(_ => IO.println("Releasing"))
  .compile
  .drain
  .unsafeRunSync()

Stream
  .bracket(IO.println("Acquiring").as(1))(n => IO.println(s"Releasing $n"))
  .flatMap { n =>
    Stream.eval(IO.println(s"Using $n"))
  }
  .compile
  .drain
  .unsafeRunSync()

Stream
  .bracket(IO.println("Acquiring").as(1))(n => IO.println(s"Releasing $n"))
  .flatMap { n =>
    Stream.eval(IO.println(s"Using $n"))
  }
  .repeatN(3)
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .take(2)
  .debug()
  .onFinalize(IO.println("Finished"))
  .compile
  .toList
  .unsafeRunSync()

Stream(1, 2, 3)
  .take(2)
  .debug()
  .onFinalizeCase(exitCase => IO.println(s"Finished with $exitCase"))
  .compile
  .toList
  .unsafeRunSync()

Stream
  .raiseError[IO](new Error("Boom!"))
  .onFinalizeCase(exitCase => IO.println(s"Finished with $exitCase"))
  .compile
  .toList
  .unsafeRunSync()

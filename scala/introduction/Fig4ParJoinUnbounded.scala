import fs2.*
import cats.effect.IO
import aquascape.*
import scala.concurrent.duration.*

object Fig4ParjoinUnbounded extends WorkshopAquascapeApp {
  def stream(using Scape[IO]): IO[Unit] = {
    // Each element is a String

    given cats.Show[Stream[IO, String]] = _.toString
    Stream[IO, String]("little-d", "hard-t")
      .stage("upstream", "upstream")
      .map(title =>
        Stream(s"T:$title-word1", s"T:$title-word2")
          .spaced[IO](1.second)
          .stage(s"$title-words", title)
          .fork("root", title)
        // Each element is a character
      )
      .stage("map", "upstream")
      .fork("root", "upstream")
      .parJoinUnbounded
      .stage("parJoinUnbounded(…)")
      .compile
      .last
      .compileStage("compile.last")
      .void
  }
}

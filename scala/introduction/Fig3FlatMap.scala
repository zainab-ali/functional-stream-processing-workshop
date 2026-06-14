import fs2.*
import cats.effect.IO
import aquascape.*

object Fig3FlatMap extends WorkshopAquascapeApp {
  def stream(using Scape[IO]): IO[Unit] = {
    Stream("ab", "cd")
      .stage("Stream(ab, cd)")
      // `Stream.emits(str.toList)` constructs a stream of characters.
      .flatMap(str =>
        Stream.emits(str.toList).stage(s"Stream.emits(${str}.toList)")
      )
      .stage("flatMap(…)")
      .compile
      .toList
      .compileStage("compile.toList")
      .void
  }
}

import fs2.io.file.Path
import fs2.*

import fs2.io.file.Files
import cats.effect.IO
import cats.effect.unsafe.implicits.global

Files[IO].list(Path("data")).compile.toList.unsafeRunSync()
Files[IO]
  .readUtf8Lines(Path("data/little-dorrit.txt"))
  .take(10)
  .compile
  .toList
  .unsafeRunSync()

import fs2.*
import cats.effect.*
import cats.effect.unsafe.implicits.global

Stream(1, 2, 3)
  .take(2)
  .compile
  .count

Stream(1, 2, 3)
  .debugChunks()
  .take(2)
  .compile
  .count

import fs2.io.file.*

Files[IO]
  .readAll(
    Path("data/little-dorrit.txt"),
    chunkSize = 64 * 1024,
    flags = Flags.Read
  )
  .debugChunks(formatter = chunk => s"The chunk size is ${chunk.size}")
  .take(131072)
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3).debugChunks().compile.count

Stream.range(0, 3).debugChunks().compile.count

Stream
  .range(0, 3)
  .chunkMin(2)
  .debugChunks()
  .compile
  .count

Stream(1, 2, 3)
  .evalTap(IO.println)
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTap(IO.println)
  .take(2)
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTapChunk(IO.println)
  .compile
  .drain

Stream(1, 2, 3)
  .evalTapChunk(IO.println)
  .take(2)
  .compile
  .toList
  .unsafeRunSync()

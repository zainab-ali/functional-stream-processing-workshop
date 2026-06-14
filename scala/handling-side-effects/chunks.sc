import cats.effect.unsafe.implicits.global

import fs2._
import cats.effect.unsafe.implicits.global
import cats.effect.IO

// Stream(1, 2, 3)
//   .take(2)
//   .compile
//   .count

Stream(1, 2, 3)
  .debugChunks()
  .take(2)
  .compile
  .count

val sysOut = System.out
System.setOut(Console.out)
// For whatever reason, the debugChunks() call in the snippet doesn't behave well with the IO modifier. The output is printed to sbt's stdout instead of to the file.

import fs2.io.file.*

Files[IO]
  .readAll(
    Path("data/little-dorrit.txt"),
    chunkSize = 65536, // 64 * 1024
    flags = Flags.Read
  )
  .debugChunks(formatter = chunk => s"The chunk size is ${chunk.size}")
  .take(131072)
  .compile
  .drain
  .unsafeRunSync()

System.setOut(sysOut)

Stream(1, 2, 3).debugChunks().compile.count

Stream.range(0, 3).debugChunks().compile.count

Stream
  .range(0, 3)
  .chunkMin(2)
  .unchunks
  .debugChunks()
  .compile
  .count

Stream(1, 2, 3)
  .evalTap(IO.println)
  .compile
  .drain

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

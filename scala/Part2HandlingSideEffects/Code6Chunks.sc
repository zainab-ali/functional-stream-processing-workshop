import fs2.*
import cats.effect.*
import cats.effect.unsafe.implicits.global

Stream(1, 2, 3)
  .take(2)
  .debugChunks()
  .compile
  .count

Stream(1, 2, 3)
  .evalTap(IO.println)
  .debugChunks()
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTapChunk(IO.println)
  .debugChunks()
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTap(IO.println)
  .debugChunks()
  .take(2)
  .compile
  .drain
  .unsafeRunSync()

Stream(1, 2, 3)
  .evalTapChunk(IO.println)
  .debugChunks()
  .take(2)
  .compile
  .drain
  .unsafeRunSync()

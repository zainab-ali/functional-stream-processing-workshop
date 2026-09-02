import fs2._
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import scala.concurrent.duration._

import cats.effect.std.Queue
val queue = Queue.bounded[IO, String](2).unsafeRunSync()

queue.offer("a").unsafeRunSync()

queue.size.unsafeRunSync()

queue.offer("b").unsafeRunSync()

queue
  .offer("c")
  .timeout(100.milliseconds).unsafeRunSync()

queue.take.unsafeRunSync()

queue.offer("c").unsafeRunSync()

def producer(q: Queue[IO, String]) =
  Stream
    .iterate(0)(_ + 1)
    .evalMap(i =>
      IO.println(s"Offering $i") >> q.offer(
        s"Item-$i"
      )
    )
    .metered(1.second)

def consumer(q: Queue[IO, String]) = Stream
  .fromQueueUnterminated(q, limit = 1)
  .evalMap(text =>
    IO.println(s"Processing $text")
  )
  .metered(2.seconds)

Queue.bounded[IO, String](2)
  .flatMap { queue => 
    producer(queue)
      .merge(consumer(queue))
      .interruptAfter(20.seconds)
      .compile
      .drain
}.unsafeRunSync()
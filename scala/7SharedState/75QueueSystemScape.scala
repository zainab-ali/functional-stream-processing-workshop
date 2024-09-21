import fs2.*
import cats.effect.*
import aquascape.*
import scala.concurrent.duration.*
import cats.effect.std.Queue

object Example75 extends AquascapeApp {
  def name: String = "queue-system"

  def stream(using Scape[IO]) =
    Queue.bounded[IO, Int](2).flatMap { queue =>
      Stream
        .range(0, 4)
        .stage("Source", "upstream")
        .enqueueUnterminated(queue)
        .covaryOutput[Unit]
        .stage("enqueue", "upstream")
        .fork("root", "upstream")
        .concurrently(
          Stream
            .fromQueueUnterminated(queue)
            .stage("queue", "queue")
            .evalMap(_ => IO.sleep(1.second))
            .fork("root", "queue")
        )
        .compile
        .drain
        .compileStage("compile.drain")
    }
}

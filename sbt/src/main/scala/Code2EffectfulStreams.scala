

final class Code2EffectfulStreams$_ {
def args = Code2EffectfulStreams_sc.args$
def scriptPath = """Code2EffectfulStreams.sc"""
/*<script>*/
import cats.effect.*
import cats.effect.unsafe.implicits.global

val greetingIO = IO.println("Hello!")

import fs2.*

val greetingStream = Stream.eval(greetingIO)

val greetThriceStream = greetingStream.repeat.take(3)

val greetAndCountIO: IO[Long] = greetThriceStream.compile.count

greetAndCountIO.unsafeRunSync()

val greetThriceIO: IO[Unit] = greetThriceStream.compile.drain
greetThriceIO.unsafeRunSync()

Stream("Mao", "Owl")
  .evalMap(name => IO.println(s"Hi $name!"))
  .head
  .compile
  .drain

val count: Long = Stream(1,2,3).compile.count
val countIO: IO[Long] = Stream(1,2,3).evalMap(IO.println(_)).compile.count

Stream.exec(greetingIO)

Stream(1, 2, 3)
  .evalMap(n => IO.println(s"The number is $n."))
  .compile
  .count
  .unsafeRunSync()

import fs2.io.file.*

Files[IO].list(Path("data")).compile.toList.unsafeRunSync()

Files[IO]
  .readUtf8Lines(Path("data/hard-times.txt"))
  .take(10)
  .compile
  .toList
  .unsafeRunSync()

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code2EffectfulStreams_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code2EffectfulStreams$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code2EffectfulStreams_sc.script as `Code2EffectfulStreams`


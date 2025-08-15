

final class Code6Chunks$_ {
def args = Code6Chunks_sc.args$
def scriptPath = """Code6Chunks.sc"""
/*<script>*/
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

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code6Chunks_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code6Chunks$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code6Chunks_sc.script as `Code6Chunks`


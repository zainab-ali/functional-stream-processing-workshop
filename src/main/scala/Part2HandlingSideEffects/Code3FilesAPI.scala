package Part2HandlingSideEffects


final class Code3FilesAPI$_ {
def args = Code3FilesAPI_sc.args$
def scriptPath = """Part2HandlingSideEffects/Code3FilesAPI.sc"""
/*<script>*/
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

/*</script>*/ /*<generated>*//*</generated>*/
}

object Code3FilesAPI_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new Code3FilesAPI$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export Code3FilesAPI_sc.script as `Code3FilesAPI`


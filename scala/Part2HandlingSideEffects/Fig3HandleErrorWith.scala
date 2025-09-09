import fs2.*
import fs2.io.file.*
import cats.effect.*
import aquascape.*
import cats.syntax.all.*

object Fig3HandleErrorWith extends WorkshopAquascapeApp {
  def getContentsFromDisk(title: String): Stream[IO, String] =
    // Stub out NoSuchFileException
    Stream.raiseError[IO](new Error("NoSuchFileException"))

  def getContentsFromApi(title: String): Stream[IO, String] =
    // Stub out API call
    Stream("In", "Chancery", "London")

  def stream(using Scape[IO]) = {
    getContentsFromDisk("Bleak House")
      .stage("disk")
      .handleErrorWith { _ =>
        getContentsFromApi("Bleak House")
      }
      .stage("handleErrorWith(_ => api)")
      .take(3)
      .compile
      .count
      .compileStage("compile.count")
      .void
  }
}

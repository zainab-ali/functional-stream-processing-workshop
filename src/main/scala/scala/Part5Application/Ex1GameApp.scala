import cats.effect.*
trait GameApp[S, C] extends IOApp.Simple {

  def game: IO[Game[S, C]]

  // Implement this function
  def run: IO[Unit] = ???
}

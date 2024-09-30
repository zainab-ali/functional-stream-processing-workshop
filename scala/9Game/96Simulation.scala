import cats.effect.*
import fs2.*
import cats.effect.*
import doodle.java2d.*
import fs2.concurrent.*
import scala.concurrent.duration.*
import cats.syntax.all.*
import doodle.interact.*
import doodle.interact.syntax.all.*
import doodle.core.*
import doodle.syntax.all.*
import doodle.core.font.Font
import doodle.core.font.FontFamily
import cats.effect.std.Queue

object Example96
    extends GameAppWithErrorAndLoggingAndMetrics[Example96.GameState, Example96.Command] {

  case class Position(x: Int, y: Int)
  case class Person(name: String, position: Position)
  case class GameState(
      people: List[Person],
      fruit: List[Position]
  )
  object GameState {
    def setPosition(
        state: GameState,
        personName: String,
        position: Position
    ): GameState = {
      state.copy(people = state.people.map { person =>
        if (person.name == personName) person.copy(position = position)
        else person
      })
    }
    def storeFruit(
        state: GameState,
        fruit: Position
    ): GameState = {
      state.copy(
        fruit = state.fruit.filterNot(_ == fruit)
      )
    }
  }
  enum Command {
    case PickFruit
  }
  def makeGame(freePeople: Queue[IO, String]) = new Game[GameState, Command] {
    def init: GameState = GameState(
      people =
        List(Person("🐱", Position(0, 0)), Person("🐰", Position(30, 40))),
      fruit = List(Position(45, 10), Position(50, 50), Position(60, 30))
    )
    def render(state: GameState): Picture[Unit] = {
      val people = state.people.map { person =>
        Picture
          .text(person.name)
          .font(font =
            Font.defaultSansSerif.family(FontFamily.monospaced).size(20)
          )
          .at(person.position.x, person.position.y)
      }
      val fruit = state.fruit.map { position =>
        Picture
          .text("🍎")
          .font(font =
            Font.defaultSansSerif.family(FontFamily.monospaced).size(10)
          )
          .at(position.x, position.y)
      }
      (people ++ fruit).combineAll
    }

    def input(text: String): Option[Command] = text match {
      case "pick" => Some(Command.PickFruit)
      case _      => None
    }

    def action(
        command: Command,
        stateRef: Ref[IO, GameState]
    ): Stream[IO, Nothing] = {
      Stream.bracket(freePeople.take)(freePeople.offer).flatMap { personName =>
        Stream
          .eval(stateRef.get)
          .flatMap { state =>
            Stream
              .eval(
                state.people
                  .find(_.name == personName)
                  .liftTo[IO](new Error(s"${personName} is an impostor!"))
              )
              .flatMap { person =>
                val fruit = state.fruit.head
                walk(person.position, fruit, 10.millis)
                  .evalMap(position =>
                    stateRef
                      .update(GameState.setPosition(_, person.name, position))
                  )
                  .drain ++ Stream
                  .exec(stateRef.update(GameState.storeFruit(_, fruit)))
              }
          }
        // .handleErrorWith { case err =>
        //   Stream.exec(IO.println(s"Got error ${err.getMessage}"))
        // }
      }
    }

    def simulation(state: Ref[IO, GameState]): Stream[IO, Nothing] =
      Stream.empty

    def walk(
        from: Position,
        to: Position,
        stepTime: FiniteDuration
    ): Stream[IO, Position] = {
      def line(from: Int, to: Int): Stream[Pure, Int] = {
        val distance = (to - from).abs + 1
        val direction = (to - from).sign
        Stream.range(0, distance).map(delta => from + delta * direction)
      }
      (line(from.x, to.x).map(x => Position(x, from.y)) ++ line(from.y, to.y)
        .map(y => Position(to.x, y)))
        .metered(stepTime)
    }
  }

  def game: IO[Game[GameState, Command]] = Queue
    .unbounded[IO, String]
    .flatMap { queue =>
      val game = makeGame(queue)
      Stream
        .emits(game.init.people.map(_.name))
        .evalMap(queue.offer)
        .compile
        .drain
        .as(game)
    }

}

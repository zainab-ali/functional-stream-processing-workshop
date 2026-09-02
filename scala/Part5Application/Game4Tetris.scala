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
import cats.effect.std.Random

object Game4Tetris
    extends GameAppWithErrorAndLoggingAndMetrics[
      Game4Tetris.GameState,
      Game4Tetris.Command
    ] {

  case class Position(x: Int, y: Int)
  case class Piece(colour: Color, blocks: List[Position])
  case class GameState(
      currentPiece: Piece,
      blocks: List[Position]
  )

  object Constants {
    val leftWallX = -5
    val rightWallX = 5
    val topY = 10
    val floorY = -10
    val scaleFactor = 20

    val boundaries = {
      val leftWall =
        Picture
          .rectangle(scaleFactor, (topY - floorY) * scaleFactor)
          .at(leftWallX * scaleFactor, 0)
          .fillColor(Color.black)
      val rightWall =
        Picture
          .rectangle(scaleFactor, (topY - floorY) * scaleFactor)
          .at(rightWallX * scaleFactor, 0)
          .fillColor(Color.black)
      val floor =
        Picture
          .rectangle((rightWallX - leftWallX) * scaleFactor, scaleFactor)
          .at(0, floorY * scaleFactor)
          .fillColor(Color.black)
      List(leftWall, rightWall, floor)
    }

    val firstPiece = Piece(
      Color.red,
      List(
        Position(0, topY),
        Position(1, topY)
      )
    )

    val pieces = {
      val colours = List(Color.blue, Color.red)
      val blocks = List(
        // 3-block line
        List(
          Position(0, topY),
          Position(1, topY),
          Position(2, topY)
        ),
        // 4-block square
        List(
          Position(0, topY),
          Position(1, topY),
          Position(0, topY - 1),
          Position(1, topY - 1)
        ),
        // L-shape
        List(
          Position(0, topY),
          Position(0, topY - 1),
          Position(1, topY - 1),
          Position(2, topY - 1)
        )
      )
      Stream
        .emits(colours)
        .repeat
        .zipWith(Stream.emits(blocks).repeat)(Piece(_, _))
    }
  }

  object GameState {
    def moveX(
        state: GameState,
        dx: Int
    ): GameState = {
      val shouldNotMove = state.currentPiece.blocks.exists { position =>
        val nextX = position.x + dx
        nextX <= Constants.leftWallX || nextX >= Constants.rightWallX
      }
      if (shouldNotMove) {
        state
      } else {
        state.copy(currentPiece =
          state.currentPiece.copy(
            blocks = state.currentPiece.blocks.map { position =>
              position.copy(x = position.x + dx)
            }
          )
        )
      }
    }

    def moveDown(state: GameState): GameState = {
      state.copy(currentPiece =
        state.currentPiece.copy(
          blocks = state.currentPiece.blocks.map { position =>
            position.copy(y = position.y - 1)
          }
        )
      )
    }

    def addNewPiece(state: GameState, piece: Piece): GameState = {
      state.copy(
        currentPiece = piece,
        blocks = state.blocks ++ state.currentPiece.blocks
      )
    }

    def cannotMoveDown(state: GameState): Boolean = {
      val nextPiecePositions = state.currentPiece.blocks.map { position =>
        position.copy(y = position.y - 1)
      }
      val hasReachedBottom = nextPiecePositions.exists(_.y <= Constants.floorY)

      val hasReachedBlock = state.blocks.exists { blockPosition =>
        nextPiecePositions.exists { piecePosition =>
          piecePosition.x == blockPosition.x && piecePosition.y == blockPosition.y
        }
      }
      hasReachedBottom || hasReachedBlock
    }

  }

  enum Command {
    case Left
    case Right
  }

  def makeGame(upcomingPieces: Queue[IO, Piece]) = new Game[GameState, Command] {
    def init: GameState = GameState(
      currentPiece = Constants.firstPiece,
      blocks = List.empty
    )

    def render(state: GameState): Picture[Unit] = {
      import Constants.scaleFactor
      val currentPiece = state.currentPiece.blocks.map { position =>
        Picture
          .square(scaleFactor)
          .at(position.x * scaleFactor, position.y * scaleFactor)
          .fillColor(state.currentPiece.colour)
      }
      val blocks = state.blocks.map { position =>
        Picture
          .square(scaleFactor)
          .at(position.x * scaleFactor, position.y * scaleFactor)
          .fillColor(Color.grey)
      }
      (Constants.boundaries ++ blocks ++ currentPiece).combineAll
    }

    def input(text: String): Option[Command] = text match {
      case "a" =>
        Some(Command.Left)
      case "d" =>
        Some(Command.Right)
      case _ => None
    }

    def action(
        command: Command,
        stateRef: Ref[IO, GameState]
    ): Stream[IO, Nothing] = {
      val dx = command match {
        case Command.Left  => -1
        case Command.Right => 1
      }
      Stream.exec(stateRef.update { state =>
        GameState.moveX(state, dx)
      })
    }

    def simulation(stateRef: Ref[IO, GameState]): Stream[IO, Nothing] = {
      val movePieceDown = stateRef.get
        .flatMap { state =>
          if (GameState.cannotMoveDown(state)) {
            upcomingPieces.take.flatMap { newPiece =>
              val nextState = GameState.addNewPiece(state, newPiece)
              stateRef.set(nextState)
            }
          } else {
            stateRef.set(GameState.moveDown(state))
          }
        }

      val addNewPieces = Constants.pieces
        .evalMap(upcomingPieces.offer)
        .drain

      Stream
        .eval(movePieceDown)
        .repeat
        .metered(200.milliseconds)
        .drain
        .merge(addNewPieces)
    }
  }

  def game: IO[Game[GameState, Command]] =
    Queue.bounded[IO, Piece](1).map(makeGame(_))
}

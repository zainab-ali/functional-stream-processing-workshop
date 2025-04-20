package cheatsheet

import fs2.Stream
import fs2.io.file.Files
import fs2.io.file.Path
import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all.*

object WriteCheatSheet extends IOApp.Simple {

  def run = {
    val cheatSheet = CheatSheet.default
    validateCheatSheet(cheatSheet) >> writeCheatSheet(cheatSheet)
  }

  def validateCheatSheet(cheatSheet: CheatSheet): IO[Unit] = {
    val extractedMethodCalls =
      MethodCallMacro.extractMethodCalls.toSet
    val knownCalls =
      (cheatSheet.parts.flatMap(
        _.sections.flatMap(_.methods)
      ) ++ cheatSheet.omittedMethodCalls)
        .filterNot(cheatSheet.addedMethodCalls.contains(_))
        .toSet
    val newMethods = extractedMethodCalls
      .diff(knownCalls)
      .map(name => s""" "$name" """)
      .mkString("List(", ",\n", ")")
    val redundantMethods = knownCalls
      .diff(extractedMethodCalls)
      .map(name => s""" "$name" """)
      .mkString("List(", ",\n", ")")
    IO.raiseError(
      new Error(s"New methods found:\n${newMethods}\n${redundantMethods}")
    ).whenA(extractedMethodCalls != knownCalls)
  }

  def writeCheatSheet(cheatSheet: CheatSheet): IO[Unit] = {
    val html = HtmlOutput.htmlString(cheatSheet)
    Stream(html)
      .through(Files[IO].writeUtf8(Path("cheatsheet.html")))
      .compile
      .drain
  }

}

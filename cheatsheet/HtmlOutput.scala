package cheatsheet
import scalatags.stylesheet.*

object Style extends StyleSheet {
  import scalatags.Text.all.*
  import scalatags.stylesheet.*
  initStyleSheet()

  private val lightSecondaryColour = "#ffe98f"
  private val lightPrimaryColour = "#0e7c7b"
  private val lighterPrimaryColour = "#2a8e7d"
  private val tertiaryColour = "#f04d4d"
  private val lightTextColour = "white"

  val body = cls(
    backgroundColor := lightSecondaryColour,
    padding := "0",
    margin := "0"
  )
  val cheatsheet = cls(
    display := "flex",
    flexWrap := "wrap",
    justifyContent := "space-evenly"
  )

  val section = cls(
    display := "flex",
    flexDirection := "column",
    alignItems := "center",
    padding := "20px",
    margin := "10px",
    fontSize := "16px",
    backgroundColor := lightPrimaryColour,
    color := lightTextColour
  )

  val mainHeading = cls(
    fontFamily := "Source Sans Pro",
    fontWeight := "normal",
    css("font-variant") := "all-small-caps",
    padding := "10px",
    display := "flex",
    justifyContent := "space-between",
    backgroundColor := tertiaryColour,
    color := lightTextColour
  )

  val sectionHeading = cls(
    fontFamily := "Source Sans Pro",
    fontWeight := "normal",
    css("font-variant") := "all-small-caps",
    margin := "0"
  )

  val sectionList = cls(
    listStyle := "none",
    padding := "0",
    margin := "0"
  )
  val sectionListItem = cls.pseudoExtend("nth-child(odd)")(
    backgroundColor := lighterPrimaryColour
  )
  val footer = cls(
    display := "flex",
    justifyContent := "space-between",
    padding := "0 10px",
    fontFamily := "Source Sans Pro",
    color := lightTextColour,
    backgroundColor := tertiaryColour
  )
  val bookLink = cls(
    fontFamily := "Source Sans Pro",
    css("font-variant") := "all-small-caps",
    textDecoration := "none",
    color := lightTextColour
  )

  val twoColumns = cls(
    css("columns") := "2",
    css("-webkit-columns") := "2",
    css("-moz-columns") := "2"
  )
}

object HtmlOutput {

  def htmlString(cheatSheet: CheatSheet): String = {
    import scalatags.Text.all._
    val frag = html(
      head(
        tag("style")(Style.styleSheetText)
      ),
      body(
        Style.body,
        h1(
          Style.mainHeading,
          a(
            Style.bookLink,
            href := "https://pureasync.gumroad.com/l/functional-stream-processing-in-scala",
            target := "_blank",
            "Functional Stream Processing In Scala: fs2 cheat sheet"
          )
        ),
        div(
          Style.cheatsheet,
          cheatSheet.parts.flatMap(_.sections).map {
            case Section(sectionName, methods) =>
              val cssCls = if (methods.size > 10) {
                List(Style.twoColumns)
              } else {
                Nil
              }
              div(
                Style.section,
                h1(Style.sectionHeading, sectionName),
                ol(
                  Style.sectionList,
                  cssCls,
                  methods.map(m => li(Style.sectionListItem, code(m)))
                )
              )
          }
        ),
        footer(
          Style.footer,
          span("Licensed under CC BY 4.0 by Zainab Ali, 2025"),
          span("Published by Pure Async LTD")
        )
      )
    )

    frag.toString
  }

}

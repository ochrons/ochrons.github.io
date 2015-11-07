package example

import org.scalajs.dom
import org.scalajs.dom.raw.Event
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object ScalaJSExample {

  val lastWordPattern = """(.*)\s+(.*)""".r
  def wordToPos(string: String): String = {
    string match {
      case lastWordPattern(_, word) => word
      case _ => ""
    }
  }

  @JSExport
  def main(root: dom.html.Div): Unit = {
    val textBox = input(placeholder := "Country").render
    val result = div.render

    textBox.onkeyup = (e: Event) => {
      result.textContent = wordToPos(textBox.value)
    }
    val content =
      div(cls := "container-fluid",
        div(cls := "row",
          div(cls := "col-xs-12",
            h2("Hello world!"),
            textBox,
            result
          )
        )
      ).render
    root.innerHTML = ""
    root.appendChild(content)
  }

  val countries = Seq(
    "Macau",
    "Macedonia",
    "Madagascar",
    "Malawi",
    "Malaysia",
    "Maldives",
    "Mali",
    "Malta",
    "Marshall Islands",
    "Mauritania",
    "Mauritius",
    "Mexico",
    "Micronesia",
    "Moldova",
    "Monaco",
    "Mongolia",
    "Montenegro",
    "Morocco",
    "Mozambique"

  )
}

package figures.ui

//import scala.events._
import scala.react._
import scala.swing.event._
import scala.swing.Component

import java.awt.Point

trait ComponentEvents extends Component {
/*
  imperative evt mouseClicked[Point]
  imperative evt leftMouseClicked[Point]
  imperative evt rightMouseClicked[Point]
  imperative evt mouseMoved[Point]
  imperative evt mousePressed[Point]
  imperative evt leftMousePressed[Point]
  imperative evt rightMousePressed[Point]
  imperative evt mouseReleased[Point]
  imperative evt leftMouseReleased[Point]
  imperative evt rightMouseReleased[Point]
  imperative evt mouseDragged[Point]
  imperative evt deletePressed[Unit]
*/
	val mouseClicked = EventSource[Point]
  val leftMouseClicked = EventSource[Point]
  val rightMouseClicked = EventSource[Point]
  val mouseMoved = EventSource[Point]
  val mousePressed = EventSource[Point]
  val leftMousePressed = EventSource[Point]
  val rightMousePressed = EventSource[Point]
  val mouseReleased = EventSource[Point]
  val leftMouseReleased = EventSource[Point]
  val rightMouseReleased = EventSource[Point]
  val mouseDragged = EventSource[Point]
  val deletePressed = EventSource[Unit]

  listenTo(mouse.clicks, mouse.moves)

  reactions += {
    case MouseClicked(_, point, mods, _, _) => 
      mouseClicked emit point
      if(mods == 0)
        leftMouseClicked emit point
      else if(mods == Key.Modifier.Meta)
        rightMouseClicked emit point
    case MousePressed(_, point, mods, _, _) => 
      mousePressed emit point
      if(mods == 1024)
        leftMousePressed emit point
      else if(mods == 4096)
        rightMousePressed emit point
    case MouseReleased(_, point, mods, _, _) => 
      mouseReleased emit point
      if(mods == 0)
        leftMouseReleased emit point
      else if(mods == 256)
        rightMouseReleased emit point
    case MouseMoved(_, point, _) => mouseMoved emit point
    case MouseDragged(_, point, _) => mouseDragged emit point
  }

}

// vim: set ts=4 sw=4 et:

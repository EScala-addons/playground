package figures.ui

import scala.events._
import scala.swing.event._
import scala.swing.Component

import java.awt.Point

trait ComponentEvents extends Component {

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

  mouse.clicks.reactions += {
    case MouseClicked(_, point, mods, _, _) => 
      mouseClicked(point)
      if(mods == 0)
        leftMouseClicked(point)
      else if(mods == Key.Modifier.Meta)
        rightMouseClicked(point)
    case MousePressed(_, point, mods, _, _) => 
      mousePressed(point)
      if(mods == 1024)
        leftMousePressed(point)
      else if(mods == 4096)
        rightMousePressed(point)
    case MouseReleased(_, point, mods, _, _) => 
      mouseReleased(point)
      if(mods == 0)
        leftMouseReleased(point)
      else if(mods == 256)
        rightMouseReleased(point)
  }

  mouse.moves.reactions += {
    case MouseMoved(_, point, _) => mouseMoved(point)
    case MouseDragged(_, point, _) => mouseDragged(point)
  }

}

// vim: set ts=4 sw=4 et:

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
  imperative evt mouseReleased[Point]
  imperative evt mouseDragged[Point]

  mouse.clicks.reactions += {
    case MouseClicked(_, point, mods, _, _) => 
      mouseClicked(point)
      if(mods == 0)
        leftMouseClicked(point)
      else if(mods == Key.Modifier.Meta)
        rightMouseClicked(point)
    case MousePressed(_, point, _, _, _) => mousePressed(point)
    case MouseReleased(_, point, _, _, _) => mouseReleased(point)
  }

  mouse.moves.reactions += {
    case MouseMoved(_, point, _) => mouseMoved(point)
    case MouseDragged(_, point, _) => mouseDragged(point)
  }

}

// vim: set ts=4 sw=4 et:

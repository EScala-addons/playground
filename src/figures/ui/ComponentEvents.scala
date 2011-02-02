package figures.ui

import scala.events._
import scala.swing.event._
import scala.swing.Component

import java.awt.Point

trait ComponentEvents {
  this: Component =>

  imperative evt mouseClicked[Point]
  imperative evt mouseMoved[Point]
  imperative evt mousePressed[Point]
  imperative evt mouseReleased[Point]
  imperative evt mouseDragged[Point]

  mouse.clicks.reactions += {
    case MouseClicked(_, point, mods, _, _) => println(mods); mouseClicked(point)
    case MousePressed(_, point, _, _, _) => mousePressed(point)
    case MouseReleased(_, point, _, _, _) => mouseReleased(point)
  }

  mouse.moves.reactions += {
    case MouseMoved(_, point, _) => mouseMoved(point)
    case MouseDragged(_, point, _) => mouseDragged(point)
  }

}

// vim: set ts=4 sw=4 et:

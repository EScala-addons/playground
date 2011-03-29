package figures.model

import java.awt.{Graphics,Color,Rectangle,Point}
import scala.react._

class RectangleFigure (var rect: Rectangle) extends Figure {
  //override evt resized[Unit] = afterExec(resize) || afterExec(setBounds)
  //override evt moved[Unit] = super.moved || afterExec(setBounds)
  val afterExecResize = new EventSource[Unit]
	val afterExecSetBounds = new EventSource[Unit]
	
	override val resized = afterExecResize merge afterExecSetBounds
	override val moved = afterExecMoveBy merge afterExecSetBounds
  
  def getBounds() : Rectangle = rect 
  
  def moveBy(dx : Int, dy : Int) = {
    rect.translate(dx, dy)
    afterExecMoveBy emit ()
  }

  def resize(sx : Int, sy : Int) = {
    rect.setSize(sx, sy)
    afterExecResize emit ()
  }
  
  def setBounds(bounds : Rectangle) = {
    rect = bounds
    afterExecSetBounds emit ()
  }

  def render(g: Graphics) {
    val (x, y, width, height) = (rect.x, rect.y, rect.width, rect.height)
    g.setColor(new Color(color))
    g.fillRect(x, y, width, height)
  }

  def contains(p: Point) = getBounds.contains(p)

}

// vim: set ts=4 sw=4 et:

package figures.model

import java.awt.{Graphics,Color,Rectangle,Point}
import java.awt.geom.Ellipse2D

class EllipseFigure(var point: Point, var width: Int, var height: Int) extends Figure {

  override evt resized[Unit] = afterExec(resize) || afterExec(setBounds)
  override evt moved[Unit] = super.moved || afterExec(setBounds)
  
  def getBounds() : Rectangle = 
    new Rectangle(point.x, point.y, width, height)
  
  def moveBy(dx : Int, dy : Int) =
    point.translate(dx, dy)

  def resize(sx : Int, sy : Int) {
    width = sx
    height = sy
  }
  
  def setBounds(bounds : Rectangle) {
    point = bounds.getLocation
    width = bounds.width
    height = bounds.height
  }

  def render(g: Graphics) {
    g.setColor(new Color(color))
    g.fillOval(point.x, point.y, width, height)
  }

  def contains(p: Point) =
    new Ellipse2D.Double(point.x, point.y, width, height).contains(p)

}

// vim: set ts=4 sw=4 et:

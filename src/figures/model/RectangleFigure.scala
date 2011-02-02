package figures.model

import java.awt.{Graphics,Color,Rectangle,Point}

class RectangleFigure (var rect: Rectangle) extends Figure {
  override evt resized[Unit] = afterExec(resize) || afterExec(setBounds)
  override evt moved[Unit] = super.moved || afterExec(setBounds)
  
  def getBounds() : Rectangle = rect 
  
  def moveBy(dx : Int, dy : Int) =
    rect.translate(dx, dy)

  def resize(sx : Int, sy : Int) =
    rect.setSize(sx, sy)
  
  def setBounds(bounds : Rectangle) = 
    rect = bounds

  def render(g: Graphics) {
    val (x, y, width, height) = (rect.x, rect.y, rect.getSize.getWidth.toInt, rect.getSize.getHeight.toInt)
    g.setColor(new Color(color))
    g.fillRect(x, y, width, height)
    g.drawRect(x, y, width, height)
  }

  def contains(p: Point) = getBounds.contains(p)

}

// vim: set ts=4 sw=4 et:

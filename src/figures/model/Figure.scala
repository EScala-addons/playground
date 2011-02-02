package figures.model

import java.awt.{Graphics,Rectangle,Point}

abstract class Figure {
  evt resized[Unit] 
  evt moved[Unit] = afterExec(moveBy)
  evt geomChanged[Unit] = resized || moved
  evt changed[Unit] = geomChanged || afterExec(setColor)
  evt invalidated[Rectangle] = changed.map((_: Unit) => getBounds)
  
  protected[this] var _color = 0
  def color = _color
  def setColor(c: Int) {
    _color = c
  }
  
  def getBounds(): Rectangle
  def moveBy(dx: Int, dy: Int)  

  def render(g: Graphics)

  /** Indicates whether this Figure contains the given point */
  def contains(p: Point): Boolean

}

// vim: set ts=4 sw=4 et:

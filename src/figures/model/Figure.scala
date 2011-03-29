package figures.model

import java.awt.{Graphics,Rectangle,Point}
import scala.react._

abstract class Figure {
	
	// instead of var moved = afterExec(moveBy)
  val afterExecMoveBy = new EventSource[Unit]
  
	val resized : Events[Unit]
  val moved : Events[Unit]
 
  // must be lazy or NullPointerException..
  lazy val geomChanged : Events[Unit] = resized merge moved
  lazy val changed : Events[Unit] = geomChanged merge afterExecColor
  lazy val invalidated : Events[Rectangle] = changed.map((_:Any) => getBounds)
  
  val afterExecColor = new EventSource[Unit]
	
  //evt resized[Unit] 
  //evt moved[Unit] = afterExec(moveBy)
  //evt geomChanged[Unit] = resized || moved
  //evt changed[Unit] = geomChanged || afterExec(setColor)
  //evt invalidated[Rectangle] = changed.map((_: Unit) => getBounds)
  
  protected[this] var _color = 0
  def color = _color
  def setColor(c: Int) {
    _color = c
    afterExecColor emit ()
  }
  
  def getBounds(): Rectangle
  def moveBy(dx: Int, dy: Int)  

  def render(g: Graphics)

  /** Indicates whether this Figure contains the given point */
  def contains(p: Point): Boolean

}

// vim: set ts=4 sw=4 et:

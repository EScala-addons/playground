package figures.model

import scala.collection.mutable.ListBuffer

import java.awt.{Graphics,Color,Point,Rectangle}

class PolylineFigure(from: Point, to : Point) extends Figure {
  protected[this] var points = new ListBuffer[Point]   
  points += from
  points += to

  def this() = this(new Point(0,0), new Point(0,0))    
  
  protected[this] evt pointsChanged[Unit] = 
    afterExec(changePoint) || afterExec(insertPoint)
    
  override evt resized[Unit] = pointsChanged
  override evt moved[Unit] = super.moved || pointsChanged
  
  def getBounds = {
    var minX = points(0).x
    var minY = points(0).y
    var maxX = points(0).x
    var maxY = points(0).y      
    for (pt <- points) {
      if (pt.x < minX) minX = pt.x
      if (pt.y < minY) minY = pt.y
      if (pt.x > maxX) maxX = pt.x
      if (pt.y > maxY) maxY = pt.y
    }        
    new Rectangle(minX, minY, maxX - minX, maxY - minY)      
  }
  
  def moveBy(dx : Int, dy : Int) {
    for (pt <- points) {
      pt.translate(dx, dy)
    }  
  }
  
  def changeStart(pt : Point) {
    changePoint(0, pt)  
  }
  
  def changeEnd(pt : Point) {
    changePoint(points.length - 1, pt)  
  }
  
  def changePoint(index : Int, pt : Point) { 
    points(index) = pt
  }
  
  def insertPoint(index : Int, pt : Point) { 
    points.insert(index, pt)
  }

  def render(g: Graphics) {
    var previous = points.head // we are sure it exists because it is passed to the constructor and there is no method to remove points
    g.setColor(new Color(color))
    points.foreach { p =>
      g.drawLine(previous.x, previous.y, p.x, p.y)
      previous = p
    }
  }

  def contains(p: Point) = points.contains(p)

}

// vim: set ts=4 sw=4 et:

package figures.model

import java.awt.{Point,Rectangle}

abstract class Connector(val start: Figure, 
                         val end: Figure, 
                         protected[this] val connFigure: PolylineFigure) {

  /** This event is triggered when an endpoint of this connector is removed */
  evt endpointRemoved[Unit]

  updateStart()
  updateEnd()
  
  start.geomChanged += updateStart _
  end.geomChanged += updateEnd _
  endpointRemoved += removeConn _
  endpointRemoved += cleanup _
      
  observable def updateStart() =
    connFigure.changeStart(center(start.getBounds)) 
  
  observable def updateEnd() =
    connFigure.changeEnd(center(end.getBounds))

  def getBounds() = connFigure.getBounds

  def removeConn()

  private def cleanup() {
    start.geomChanged -= updateStart _
    end.geomChanged -= updateEnd _
  }

  protected def center(rect: Rectangle) =
    new Point(rect.x + rect.width / 2, rect.y + rect.height / 2)
}

// vim: set ts=4 sw=4 et:

package figures.model

import java.awt.{Point,Rectangle}

class Connector(val start: Figure, 
                val end: Figure, 
                protected[this] val connFigure: PolylineFigure) {
                    
  updateStart()
  updateEnd()
  
  start.geomChanged += updateStart _
  end.geomChanged += updateEnd _
      
  observable def updateStart() =
    connFigure.changeStart(center(start.getBounds)) 
  
  observable def updateEnd() =
    connFigure.changeEnd(center(end.getBounds))

  private def center(rect: Rectangle) =
    new Point(rect.x + rect.width / 2, rect.y + rect.height / 2)
}

// vim: set ts=4 sw=4 et:

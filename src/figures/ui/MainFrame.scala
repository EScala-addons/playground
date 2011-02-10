package figures.ui

import scala.events._
import scala.swing.{MainFrame,BorderPanel,FlowPanel,Panel}
import scala.swing.event.MouseClicked

import java.awt.{Color,Rectangle,Point,Dimension,Graphics2D}

import figures.model.{Figure,MutableDrawing}

class FigureFrame extends MainFrame {

  title = "EScala Figures"

  lazy val canvas = new Panel with Canvas with FigureEventsManager {
    // the model
    val drawing = new MutableDrawing

    background = Color.WHITE
    preferredSize = new Dimension(1024, 768)

    def drawingInvalidated(rect: Rectangle) {
      // only repaint the area containing the old and new positions
      repaint(toClear.foldLeft(rect)(_.union(_)))
    }

    evt invalidated = drawing.invalidated || (figureSelected || figureUnselected || figureDropped).map((f: Figure) => f.getBounds)

    invalidated += drawingInvalidated _

    override def paint(g: Graphics2D) {
      // first clear areas
      for(r <- toClear) {
        g.setColor(background)
        g.fillRect(r.x, r.y, r.width, r.height)
      }
      toClear.clear()
      
      // draw all the figures
      drawing.figures.foreach { f =>
        f.render(g)
      }
      super.paint(g)
    }
  }

  lazy val buttons = new FlowPanel

  lazy val panel = new BorderPanel {
    background = Color.WHITE
    layout += ((buttons -> BorderPanel.Position.North),
        (canvas -> BorderPanel.Position.Center))
  }

  contents = panel
  
  maximize

}

// vim: set ts=4 sw=4 et:

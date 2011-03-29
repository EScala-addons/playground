package figures.ui

//import scala.events._
import scala.react._
import scala.swing.{MainFrame,BorderPanel,FlowPanel,Panel,Component}
import scala.swing.event.MouseClicked

import java.awt.{Color,Rectangle,Point,Dimension,Graphics2D}

import figures.model.{Figure,MutableDrawing}

class FigureFrame extends MainFrame with Observing {

  title = "EScala Figures"

  lazy val canvas = new Panel with Canvas with FigureEventsManager {
    // the model
    val drawing = new MutableDrawing

    background = Color.WHITE
    preferredSize = new Dimension(1024, 768)

    def drawingInvalidated(rect: Rectangle) {
      // only repaint the area containing the old and new positions
      val area = toClear.foldLeft(rect)(_.union(_))
      toClear.clear()
      repaint(area)
    }

    //evt invalidated = drawing.invalidated || (figureSelected || figureUnselected || figureDropped).map((f: Figure) => f.getBounds)
    val invalidated = drawing.invalidated merge (figureSelected merge figureUnselected merge figureDropped).map((f: Figure) => f.getBounds)

    //invalidated += drawingInvalidated _
    val obInv = observe(invalidated) { x => drawingInvalidated _; true }

    override def paint(g: Graphics2D) {
      // draw the background
      super.paintComponent(g)
      // draw all the figures intersecting the current area
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

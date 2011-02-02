package figures.ui

import scala.events._
import scala.swing.{MainFrame,BorderPanel,FlowPanel,Panel}
import scala.swing.event.MouseClicked

import java.awt.{Color,Rectangle,Point,Dimension,Canvas,Graphics2D}

import figures.model.MutableDrawing

class FigureFrame extends MainFrame {

  title = "EScala Figures"

  lazy val canvas = new Panel with ComponentEvents {
    // the model
    val drawing = new MutableDrawing

    background = Color.WHITE
    preferredSize = new Dimension(1024, 768)

    def drawingInvalidated(rect: Rectangle) {
      repaint(rect)
    }

    drawing.invalidated += drawingInvalidated _

    override def paint(g: Graphics2D) {
      // draw all the figures
      drawing.figures.foreach { f =>
        f.render(g)
      }
    }

    mouseClicked += selectFigure _

    /** Select the figures at the given coordinate if any */
    def selectFigure(point: Point) {
      drawing.figureAt(point) match {
        case Some(figure) => println(figure)
        case _ => // do nothing
      }
    }

  }

  lazy val buttons = new FlowPanel

  lazy val panel = new BorderPanel {
    background = Color.WHITE
    layout += ((buttons -> BorderPanel.Position.North),
        (canvas -> BorderPanel.Position.Center))
  }

  contents = panel
  
  maximized

}

// vim: set ts=4 sw=4 et:

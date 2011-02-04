package figures.ui

import scala.events._
import scala.swing.{MainFrame,BorderPanel,FlowPanel,Panel}
import scala.swing.event.MouseClicked

import java.awt.{Color,Rectangle,Point,Dimension,Canvas,Graphics2D}

import figures.model.{Figure,MutableDrawing}

class FigureFrame extends MainFrame {

  title = "EScala Figures"

  lazy val canvas = new Panel with FigureEventsManager {
    // the model
    val drawing = new MutableDrawing

    background = Color.WHITE
    preferredSize = new Dimension(1024, 768)

    def drawingInvalidated(rect: Rectangle) {
//      repaint(rect)
      repaint()
    }

    drawing.invalidated += drawingInvalidated _

    override def paint(g: Graphics2D) {
      // first clear areas
      /*toClear.foreach { r => 
        g.setColor(background)
        g.fillRect(r.x, r.y, r.width, r.height)
        g.drawRect(r.x, r.y, r.width, r.height)
      }
      toClear.clear
      revalidate()*/
      println("bounds: " + bounds)
      g.setColor(background)
      g.fillRect(0, 0, bounds.width, bounds.height)
//      g.drawRect(0, 0, bounds.width, bounds.height)
      // draw all the figures
      drawing.figures.foreach { f =>
        f.render(g)
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

package figures.ui

import scala.swing.{Button, BorderPanel}
import scala.swing.event.ButtonClicked

import java.awt.{Rectangle,Point,Dimension}

import figures.model.RectangleFigure

trait RectangleSupport {
  this: FigureFrame =>

  buttons.contents += new Button {
    text = "Add Rectangle"
    reactions += {
      case ButtonClicked(_) => addRectangle
    }
  }

  def addRectangle {
    canvas.drawing += new RectangleFigure(new Rectangle(new Point(0, 0), new Dimension(100, 100)))
  }

}

// vim: set ts=4 sw=4 et:

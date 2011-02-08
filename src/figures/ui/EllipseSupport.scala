package figures.ui

import scala.swing.{Button, BorderPanel}
import scala.swing.event.ButtonClicked

import java.awt.{Rectangle,Point,Dimension}

import figures.model.EllipseFigure

trait EllipseSupport extends FigureFrame {

  buttons.contents += new Button {
    text = "Add Ellipse"
    reactions += {
      case ButtonClicked(_) => addEllipse
    }
  }

  def addEllipse {
    canvas.drawing += new EllipseFigure(new Point(0, 0), 100, 100)
  }

}

// vim: set ts=4 sw=4 et:

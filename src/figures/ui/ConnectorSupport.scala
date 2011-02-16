package figures.ui

import scala.events.VarList
import scala.swing.Button
import scala.swing.event.ButtonClicked

import java.awt.{Point,Rectangle}

import figures.model.{Figure,Connector,PolylineFigure}

trait ConnectorSupport extends FigureFrame {

  buttons.contents += new Button {
    text = "Add Connector"
    reactions += {
      case ButtonClicked(_) => 
        canvas.figureSelected += toConnect _
    }

    private var first: Figure = null

    val connectors = new VarList[Connector]

    evt connectorChanged[Rectangle] = 
      connectors.any(c => (beforeExec(c.updateStart)
                        || beforeExec(c.updateEnd)).map(() => c.getBounds))

    connectorChanged += clearOldLine _

    def toConnect(f: Figure) {
      if(first == null) {
        first = f
      } else {
        val from = first
        val to = f
        val line = new PolylineFigure(center(from.getBounds), center(to.getBounds))
        canvas.drawing += line
        val conn = new Connector(from, to, line) {

          evt endpointRemoved[Unit] = 
            canvas.drawing.figures.elementRemoved && (f => f == start || f == end)

          def removeConn() {
            // remove the connector from the list
            println("before: " + connectors.size)
            connectors -= this
            println("after: " + connectors.size)
            // remove the line
            canvas.drawing -= line
            // unregister this reaction to free the event
            endpointRemoved -= removeConn _
          }
        }
        connectors += conn

        // do not listen to selected figures anymore
        canvas.figureSelected -= toConnect _

        // reinitialization
        first = null
      }
    }

    def clearOldLine(r: Rectangle) {
      canvas.toClear += new Rectangle(r)
    }

    private def center(rect: Rectangle) =
      new Point(rect.x + rect.width / 2, rect.y + rect.height / 2)
  }
}

// vim: set ts=4 sw=4 et:

package figures.ui

import figures.EventOperators
import figures.model.{Figure,MutableDrawing}

import scala.collection.mutable.ListBuffer
import scala.swing.{Action,Menu,MenuItem}

import java.awt.{Point,Rectangle,Color,Graphics2D}
import javax.swing.{JColorChooser,JPopupMenu}

trait FigureEventsManager extends ComponentEvents with Canvas with EventOperators {
  self =>

  val toClear = new ListBuffer[Rectangle]

  /** This event is triggered whenever a figure is selected with a left or right click */
  evt figureSelected[Figure] = some(leftMouseClicked.map((p: Point) => drawing.figureAt(p))) || dropSecond(rightSelected)

  /** This event is triggered whenever a figure is selected with a right click */
  evt rightSelected[Figure,Point] = some(rightMouseClicked.map((p: Point) => drawing.figureAt(p))) and rightMouseClicked

  evt noneSelected[Unit] = none(leftMousePressed.map((p: Point) => drawing.figureAt(p)))

  evt someSelected[Figure] = some(leftMousePressed.map((p: Point) => drawing.figureAt(p)))

  /** This event is triggered whenever a previously selected figure is unselected */
  evt figureUnselected[Figure] = 
    (noneSelected && (() => selectedFigure != null) ||
      (someSelected && (f => selectedFigure != null && f != selectedFigure))).map(() => selectedFigure)

  /** This event is triggered whenever a figure is started to be dragged */
  evt figureDragStarted[Figure,Point] = someSelected and leftMousePressed

  /** This event is triggered whenever a figure is dragged. It provides the new position */
  evt figureDragged[Point] = mouseDragged && (() => selectedFigure != null)

  /** This event is triggered whenever the dragged figure is dropped */
  evt figureDropped[Figure] = (leftMouseReleased && (() => oldPoint != null)).map(() => selectedFigure)

  figureDragStarted += dragStart _
  figureDragged += dragging _
  figureDropped += dropping _
  figureSelected += select _
  figureUnselected += unselect _
  noneSelected += noneselect _
  rightSelected += openMenu _

  private var selectedFigure: Figure = null
  private var oldPoint: Point = null

  /** When starting to drag a figure, save the start point */
  def dragStart(f: Figure, p: Point) {
    selectedFigure = f
    oldPoint = p
  }

  def dragging(p: Point) {
    toClear += new Rectangle(selectedFigure.getBounds)
    toClear ++= currentHandles
    val (dx, dy) = (p.x - oldPoint.x, p.y - oldPoint.y)
    selectedFigure.moveBy(dx, dy)
    oldPoint = p
  }

  def dropping(f: Figure) {
    toClear ++= currentHandles
    selectedFigure = null
    oldPoint = null
  }

  def select(f: Figure) {
    selectedFigure = f
    toClear ++= currentHandles
  }

  def unselect(f: Figure) {
    toClear ++= handlesFor(f)
  }

  def noneselect() {
    selectedFigure = null
    oldPoint = null
  }

  case class Handle(cx: Int, cy: Int) extends Rectangle(cx - 5, cy - 5, 10, 10)

  private def handlesFor(f: Figure) = {
    if(f != null) {
      val bounds = f.getBounds
      val h1 = Handle(bounds.x, bounds.y)
      val h2 = Handle(bounds.x + bounds.width, bounds.y)
      val h3 = Handle(bounds.x, bounds.y + bounds.height)
      val h4 = Handle(bounds.x + bounds.width, bounds.y + bounds.height)
      List(h1, h2, h3, h4)
    } else
      Nil
  }

  def currentHandles = handlesFor(selectedFigure)

  lazy val popup: JPopupMenu = new JPopupMenu {
    add(
      new Action("Change color") {
        val chooser = new JColorChooser(Color.BLACK)
        def apply() {
          JColorChooser.showDialog(self.peer, "Change figure color", new Color(selectedFigure.color)) match {
            case null => // do nothing
            case c => selectedFigure.setColor(c.getRGB)
          }
        }
      }.peer
    )

    add(
      new Action("Delete") {
        def apply() {
          toClear ++= currentHandles
          drawing -= selectedFigure
          selectedFigure = null
        }
      }.peer
    )
  }

  def openMenu(f: Figure, p: Point) {
    selectedFigure = f
    popup.show(self.peer, p.x, p.y)
  }

  override def paint(g: Graphics2D) {
    // draw the handles
    if(selectedFigure != null) {
      val bounds = selectedFigure.getBounds
      currentHandles.foreach { h =>
        g.setColor(Color.LIGHT_GRAY)
        g.fill(h)
      }
    }
  }
}

// vim: set ts=4 sw=4 et:

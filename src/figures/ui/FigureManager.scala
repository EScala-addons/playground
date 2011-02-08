package figures.ui

import figures.model.{Figure,MutableDrawing}

import scala.collection.mutable.ListBuffer
import scala.events._
import scala.swing.{Action,Menu,MenuItem}

import java.awt.{Point,Rectangle,Color,Graphics2D}
import javax.swing.{JColorChooser,JPopupMenu}

trait FigureEventsManager extends ComponentEvents with Canvas {
  self =>

  val toClear = new ListBuffer[Rectangle]

  def some[T](ev: Event[Option[T]]): Event[T] =
    (ev && (d => d.isDefined)).map((o: Option[T]) => o.get)
  def none(ev: Event[Option[_]]): Event[Unit] =
    (ev && (d => !d.isDefined)).map((_: Option[_]) => ())

  /** This event is triggered whenever a figure is selected with a left click */
  evt figureSelected[Figure] = some(leftMouseClicked.map((p: Point) => drawing.figureAt(p)))
  evt rightSelected[Figure,Point] = some(rightMouseClicked.map((p: Point) => drawing.figureAt(p))) and rightMouseClicked

  evt figureUnselected[Figure] = (none(leftMouseClicked.map((p: Point) => drawing.figureAt(p)) && (_ => selectedFigure != null)) ||
    (figureDragStarted && (p => selectedFigure != null && p._1 != selectedFigure))).map((_: Any) => selectedFigure)

  evt figureDragStarted[Figure,Point] = some(leftMousePressed.map((p: Point) => drawing.figureAt(p))) and leftMousePressed

  evt figureDragged[Point] = mouseDragged && (() => selectedFigure != null)
  evt figureDropped[Figure] = (leftMouseReleased && (() => oldPoint != null)).map((_: Point) => selectedFigure)

  figureDragStarted += dragStart _
  figureDragged += dragging _
  figureDropped += dropping _
  figureSelected += select _
  figureUnselected += unselect _
  rightSelected += openMenu _

  private var selectedFigure: Figure = null
  private var oldPoint: Point = null
  /** When starting to drag a figure, save the start point */
  def dragStart(f: Figure, p: Point) {
    selectedFigure = f
    oldPoint = p
  }

  def dragging(p: Point) {
    toClear += selectedFigure.getBounds.clone.asInstanceOf[Rectangle]
    currentHandles.foreach { h =>
      toClear += h
    }
    val (dx, dy) = (p.x - oldPoint.x, p.y - oldPoint.y)
    selectedFigure.moveBy(dx, dy)
    oldPoint = p
  }

  def dropping(f: Figure) {
    currentHandles.foreach { h =>
      toClear += h
    }
    selectedFigure = null
    oldPoint = null
  }

  def select(f: Figure) {
    selectedFigure = f
    currentHandles.foreach { h =>
      toClear += h
    }
  }

  def unselect(f: Figure) {
    handlesFor(f).foreach { h =>
      toClear += h
    }
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
//        g.setColor(Color.BLACK)
//        g.draw(h)
      }
    }
  }

}

// vim: set ts=4 sw=4 et:

package figures.ui

import scala.react._

import figures.EventOperators
import figures.model.{Figure,MutableDrawing}

import scala.collection.mutable.ListBuffer
import scala.swing.{Action,Menu,MenuItem}

import java.awt.{Point,Rectangle,Color,Graphics2D}
import javax.swing.{JColorChooser,JPopupMenu}

trait FigureEventsManager extends ComponentEvents with Canvas with EventOperators with Observing{
  self =>

  val toClear = new ListBuffer[Rectangle]

  /** This event is triggered whenever a figure is selected with a left or right click */
  val figureSelected : Events[Figure] = some(leftMouseClicked.map((p: Point) => drawing.figureAt(p))) merge dropSecond(rightSelected)

  // !!!!!!!!!!!!!!!!!!2 type params not allowed in scala.react.Events:
  /** This event is triggered whenever a figure is selected with a right click */
  val rightSelected : Events[Figure,Point] = some(rightMouseClicked.map((p: Point) => drawing.figureAt(p))) and rightMouseClicked

  val noneSelected : Events[Unit] = none(leftMousePressed.map((p: Point) => drawing.figureAt(p)))

  val someSelected : Events[Figure] = some(leftMousePressed.map((p: Point) => drawing.figureAt(p)))

  /** This event is triggered whenever a previously selected figure is unselected */
  val figureUnselected : Events[Figure] = 
    (noneSelected and (() => selectedFigure != null) merge
      (someSelected and (f => selectedFigure != null and f != selectedFigure))).map(() => selectedFigure)

	// !!!!!!!!!!!!!!!!!!2 type params not allowed in scala.react.Events:
  /** This event is triggered whenever a figure is started to be dragged */
  val figureDragStarted : Events[Figure,Point] = someSelected and leftMousePressed

  /** This event is triggered whenever a figure is dragged. It provides the new position */
  val figureDragged : Events[Point] = mouseDragged and (() => selectedFigure != null)

  /** This event is triggered whenever the dragged figure is dropped (after having been dragged) */
  val figureDropped : Events[Figure] = (leftMouseReleased and (() => oldPoint != null)).map(() => selectedFigure) after figureDragged 

	/*
  figureDragStarted += dragStart _
  figureDragged += dragging _
  figureDropped += dropping _
  figureSelected += select _
  figureUnselected += unselect _
  noneSelected += noneselect _
  rightSelected += openMenu _
  */
  val obDragStart = observe(figureDragStarted) { x => dragStart _; true }
  val obDragged = observe(figureDragged) { x => dragging _; true }
  val obDropped = observe(figureDropped) { x => dropping _; true }
  val obFigSelected = observe(figureSelected) { x => select _; true }
  val obFigUnselected = observe(figureUnselected) { x => unselect _; true }
  val obNoneSelected = observe(noneSelected) { x => noneselect _; true }
  val obRightSelected = observe(rightSelected) { x => openMenu _; true }
  

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

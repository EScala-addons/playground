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
  
  private var selectedFigure: Figure = null
  private var oldPoint: Point = null

  val toClear = new ListBuffer[Rectangle]

  /** This event is triggered whenever a figure is selected with a left or right click */
  val figureSelected : Events[Figure] = some(leftMouseClicked.map((p: Point) => drawing.figureAt(p))) // merge dropSecond(rightSelected)
  
  /** This event is triggered whenever a figure is selected with a right click */
  val rightSelected : Events[(Figure,Point)] = some(rightMouseClicked.map((p: Point) => 
                                                        (drawing.figureAt(p)) match { 
                                                            case None => None; 
                                                            case Some(f) => Some (f, p) 
                                                         }))

  val noneSelected : Events[Unit] = none(leftMousePressed.map((p: Point) => drawing.figureAt(p)))

  // some(leftMousePressed.map((p: Point) => drawing.figureAt(p)))
  val someSelected : Events[(Figure, Point)] = some(leftMousePressed.map((p: Point) => 
                                                        (drawing.figureAt(p)) match { 
                                                            case None => None; 
                                                            case Some(f) => Some (f, p) 
                                                         }))

  /** This event is triggered whenever a previously selected figure is unselected */
  val figureUnselected : Events[Figure] = 
    (noneSelected.filter (_ => self.selectedFigure != null) merge
      someSelected.filter(f => (selectedFigure != null) && (f != selectedFigure))).map(_ => selectedFigure)

  /** This event is triggered whenever a figure is started to be dragged */
  val figureDragStarted : Events[(Figure,Point)] = someSelected // and leftMousePressed -> weg weil someSelected den Punkt aus leftMousePressed enthaelt

  /** This event is triggered whenever a figure is dragged. It provides the new position */
  val figureDragged : Events[Point] = mouseDragged.filter(_ => selectedFigure != null)

  /** This event is triggered whenever the dragged figure is dropped (after having been dragged) */
  val figureDropped : Events[Figure] = (leftMouseReleased.filter(_ => oldPoint != null)).map(_ => selectedFigure) after figureDragged 

	/*
  figureDragStarted += dragStart _
  figureDragged += dragging _
  figureDropped += dropping _
  figureSelected += select _
  figureUnselected += unselect _
  noneSelected += noneselect _
  rightSelected += openMenu _
  */
  
  
  observe(figureDragStarted) { (fp: (Figure, Point)) => dragStart(fp._1, fp._2); true }
  observe(figureDragged) { p: Point => dragging(p); true }
  observe(figureDropped) { f: Figure => dropping(f); true }
  observe(figureSelected) { f: Figure => select(f); true }
  observe(figureUnselected) { f: Figure => unselect(f); true }
  observe(noneSelected) { x => noneselect _; true }
  observe(rightSelected) { (fp: (Figure, Point)) => openMenu(fp._1, fp._2); true }
  

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

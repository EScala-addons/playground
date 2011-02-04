package figures.ui

import figures.model.{Figure,MutableDrawing}

import scala.collection.mutable.ListBuffer
import scala.events.Event

import java.awt.{Point,Rectangle}

trait FigureEventsManager extends ComponentEvents {

  val drawing: MutableDrawing
  //val toClear = new ListBuffer[Rectangle]
  val toClear = new ListBuffer[Rectangle]

  def some[T](ev: Event[Option[T]]): Event[T] =
    (ev && (d => d.isDefined)).map((o: Option[T]) => o.get)

  /** This event is triggered whenever a figure is selected with a left click */
  evt figureSelected[Figure] = some(leftMouseClicked.map((p: Point) => drawing.figureAt(p)))

  evt figureDragStarted[Figure,Point] = some(leftMousePressed.map((p: Point) => drawing.figureAt(p))) and leftMousePressed
  //evt figureDragStarted[Figure,Point] = (leftMousePressed && (drawing.figureAt(_).isDefined)).map((p: Point) => (drawing.figureAt(p).get, p))

  evt figureDragged[Point] = mouseDragged && (() => draggedFigure != null)
  evt figureDropped[Unit] = leftMouseReleased && (() => draggedFigure != null)

  figureDragStarted += dragStart _
  figureDragged += dragging _
  figureDropped += dropping _

  private var draggedFigure: Figure = null
  private var oldPoint: Point = null
  /** When starting to drag a figure, save the start point */
  def dragStart(f: Figure, p: Point) {
    draggedFigure = f
    oldPoint = p
  }

  def dragging(p: Point) {
    toClear += draggedFigure.getBounds.clone.asInstanceOf[Rectangle]
    val (dx, dy) = (p.x - oldPoint.x, p.y - oldPoint.y)
    draggedFigure.moveBy(dx, dy)
    oldPoint = p
  }

  def dropping() {
    draggedFigure = null
    oldPoint = null
  }

}

// vim: set ts=4 sw=4 et:

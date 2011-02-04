package figures.ui

import figures.model.{Figure,MutableDrawing}

import scala.collection.mutable.ListBuffer
import scala.events.Event

import java.awt.{Point,Rectangle}

trait FigureEventsManager extends ComponentEvents {

    val drawing: MutableDrawing
    val toClear = new ListBuffer[Rectangle]

    def some[T](ev: Event[Option[T]]): Event[T] =
      (ev && (d => d.isDefined)).map((o: Option[T]) => o.get)

    /** This event is triggered whenever a figure is selected with a left click */
    evt figureSelected[Figure] = some(leftMouseClicked.map((p: Point) => drawing.figureAt(p)))

    evt figureDragStarted[Figure,Point] = (leftMousePressed.map((p: Point) => (drawing.figureAt(p), p))) && (p => p._1.isDefined)

    evt figureDragged[Point] = mouseDragged && (() => draggedFigure != null)

    figureSelected += onFigureSelected _
    figureDragStarted += dragStart _
    figureDragged += dragging _

    private var draggedFigure: Figure = null
    private var oldPoint: Point = null
    /** When starting to drag a figure, save the start point */
    def dragStart(f: Figure, p: Point) {
      draggedFigure = f
      oldPoint = p
    }

    def dragging(p: Point) {
      toClear += draggedFigure.getBounds
      val (dx, dy) = (p.x - oldPoint.x, p.y - oldPoint.y)
      draggedFigure.moveBy(dx, dy)
      oldPoint = p
    }

    /** This method reacts to the selection of a figure
     */
    def onFigureSelected(f: Figure) {
      
    }
}

// vim: set ts=4 sw=4 et:

package figures.ui

import figures.model.{Figure,MutableDrawing}

import java.awt.Point

trait FigureEventsManager extends ComponentEvents {

    val drawing: MutableDrawing

    /** This event is triggered whenever a figure is selected with a left click */
    //evt figureSelected[Figure] = (leftMouseClicked && ((p: Point) => drawing.figureAt(p).isDefined)).map((p: Point) => drawing.figureAt(p).get)
    evt figureSelected[Figure] = (leftMouseClicked && (drawing.figureAt(_).isDefined)).map((p: Point) => drawing.figureAt(p).get)

    figureSelected += onFigureSelected _

    /** This method reacts to the selection of a figure
     */
    def onFigureSelected(f: Figure) {
      println("slected: " + f)
    }
}

// vim: set ts=4 sw=4 et:

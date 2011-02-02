package figures.model

import scala.events.VarList

import java.awt.Point

class MutableDrawing {
  val figures = new VarList[Figure]
  evt invalidated = 
    figures.any(_.invalidated) || 
    figures.elementAdded.map((f: Figure) => f.getBounds) ||
    figures.elementRemoved.map((f: Figure) => f.getBounds)
  
  def +=(fig: Figure) { figures += fig }
  def -=(fig: Figure) { figures -= fig }

  /** Returns the figure on top at the given coordinate
    * If no figure is at this coordinate in the drawing, returns None
    */
  def figureAt(p: Point): Option[Figure] = figures.reverse.find(_.contains(p))

}

// vim: set ts=4 sw=4 et:

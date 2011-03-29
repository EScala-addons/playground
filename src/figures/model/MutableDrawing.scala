package figures.model

//import scala.events.VarList
import scala.react._
import scala.collection.mutable.ListBuffer

//import java.awt.Point
import java.awt.{Rectangle,Point}

class MutableDrawing {
	
	/*
  val figures = new VarList[Figure]
  evt invalidated = 
    figures.any(_.invalidated) || 
    figures.elementAdded.map((f: Figure) => f.getBounds) ||
    figures.elementRemoved.map((f: Figure) => f.getBounds)
  */
  
  val figures = new ListBuffer[Figure] 
  
 	val elementRemoved = new EventSource[Figure]
  val elementAdded = new EventSource[Figure]
  
  // simulating VarList.any function...	       
	def invalidate : Events[Rectangle] = {
  		for(fig <- figures)
    		invalidated merge fig.invalidated
    	invalidated
  }
  
  var invalidated = invalidate
  
  // one time merge of add and remove imperative events
  invalidated merge elementAdded.map((f: Figure) => f.getBounds) merge elementRemoved.map((f: Figure) => f.getBounds)
  
  // Not working, exception thrown -> continuations need to be enabled
  // observe(elementAdded) { x => invalidated merge x.invalidated; true }
  // TODO: observe(elementRemoved) { x => true }
  
  def +=(fig: Figure) { figures += fig; elementAdded emit fig }
  def -=(fig: Figure) { figures -= fig; elementRemoved emit fig}

  /** Returns the figure on top at the given coordinate
    * If no figure is at this coordinate in the drawing, returns None
    */
  def figureAt(p: Point): Option[Figure] = figures.reverse.find(_.contains(p))

}

// vim: set ts=4 sw=4 et:

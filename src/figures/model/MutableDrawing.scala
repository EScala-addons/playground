package figures.model

//import scala.events.VarList
import scala.react._
import scala.collection.mutable.ListBuffer

//import java.awt.Point
import java.awt.{Rectangle,Point}

class MutableDrawing extends Observing{

  val figures = new Var(new ListBuffer[Figure]) 
  
	// COMPACT STYLE: evts defined as merging of fig.invalidated Events by each figure in figures-ListBuffer:
	// if figures-Itemscount change, signal is reevaluated since depending on figures-List
	var invalidate : Signal[Events[Rectangle]] = Signal {
		// create new Source for collecting fig.evts together by merge
		var evts : Events[Rectangle] = new EventSource[Rectangle]
		// call invalidated on each figure, then merge all fig.invalidated events together starting on empty Source:
		evts = figures().map(_.invalidated).foldLeft(evts)((pre, suc) => pre merge suc)
		evts // Return Event-Collection
	}
	
	// Flatten all event-dependent 
	var invalidated : Events[Rectangle] = invalidate.flatten
	
	// observe changes:
	val obSignal = observe(invalidate) { x => println("INVALIDATING " + x); true }
	val obEvts = observe(invalidated) { x => println("flatten evts shot " + x); true }
   	
  def +=(fig: Figure) { 
//    figures += fig; 
  	// create new instance of ListBuffer with updated count of elements for correct signal reevaluation
	val temp = new ListBuffer[Figure]
	for(figOld <- figures())
		temp.append(figOld)
	temp.append(fig)
	figures() = temp // this line activates reevaluation of signal (invalidate)
  }
  def -=(fig: Figure) { 
//    figures -= fig; 
    // create new instance of ListBuffer with updated count of elements for correct signal reevaluation
	val temp = new ListBuffer[Figure]
	for(figOld <- figures())
		if(figOld != fig)
			temp.append(figOld)
	figures() = temp // this line activates reevaluation of signal (invalidate)
  }

  /** Returns the figure on top at the given coordinate
    * If no figure is at this coordinate in the drawing, returns None
    */
  def figureAt(p: Point): Option[Figure] = figures().reverse.find(_.contains(p))

}

// vim: set ts=4 sw=4 et:


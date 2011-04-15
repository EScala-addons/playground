package extensions

import scala.react._
import scala.collection.mutable.ListBuffer

/*
 * Implementation of event sequence operator
 */
class EventNodeSequence[T, U, V](ev1: Events[T], ev2: => Events[U], merge: (T, U) => V) extends EventSource[V] with Observing{

  // the id of the last received event1
  // -1 if event1 was not received yet (or after last event2)
  var id: Int = -1
  // value of the last event1
  var v1: T = _

  val evtSequence = new EventSource[V]
  
  val afterEvt1 = new EventSource[T]
  val afterEvt2 = new EventSource[U]
  
  observe(ev1) { x:T => 
    // ignore consecutive occurrences of event1
    if (this.id == -1) {
      // save the data of event1
      this.id = 0
      this.v1 = v1
    }
    true
  }
  
  observe(ev2) { v2: U => 
    // react to event2 only if event1 was already received;
    // also ensure that event2 is different from event1 by comparing
    if (this.id == 0 && (ev1 != ev2)) {
      evtSequence emit merge(this.v1, v2)
      this.id = -1
    }
    true
  }
}

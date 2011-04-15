package figures

//import scala.events._
import scala.react._
import extensions._

/**
 * This trait defines new operators on events
 */
trait EventOperators {

  /** Transforms an event carrying an optional value to an event
      triggered whenever the value of the original event is Some */
  def some[T](ev: Events[Option[T]]): Events[T] =
    (ev.filter(d => d.isDefined)).map((o: Option[T]) => o.get)

  /** Transforms an event carrying an optional value to an event
      triggered whenever the value of the original event is None */
  def none(ev: Events[Option[_]]): Events[Unit] =
    (ev.filter(d => !d.isDefined)).map((_: Option[_]) => ())

  /** Transforms an event taking two values by dropping the second value */
  def dropSecond[T](ev: Events[(T, _)]): Events[T] =
    ev.map( (v: (T, Any)) => v._1)

  implicit def addAfterOp[T](ev: Events[T]) = new {
    def after[S >: T](other: => Events[_]) = new EventNodeSequence[Any,S,S](other, ev, (_: Any, p: S) => p)
  }

}

// vim: set ts=4 sw=4 et:

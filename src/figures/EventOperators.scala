package figures

import scala.events._

/**
 * This trait defines examples of new operators for events
 */
trait EventOperators {

  /** Transforms an event carrying an optional value to an event
      triggered whenever the value of the original event is Some */
  def some[T](ev: Event[Option[T]]): Event[T] =
    (ev && (d => d.isDefined)).map((o: Option[T]) => o.get)

  /** Transforms an event carrying an optional value to an event
      triggered whenever the value of the original event is None */
  def none(ev: Event[Option[_]]): Event[Unit] =
    (ev && (d => !d.isDefined)).map((_: Option[_]) => ())

  /** Transforms an event taking two values by dropping the second value */
  def dropSecond[T](ev: Event[(T, _)]): Event[T] =
    ev.map((v: T, _: Any) => v)

  /** Adds the `after' operator to events:
   *  `e1 after e2' is the same as `e2.then(e1, (_, p) => p)'
   */
  implicit def addAfterOp[T](ev: Event[T]) = new {
    def after[S >: T](other: => Event[_]) = new EventNodeSequence[Any,S,S](other, ev, (_: Any, p: S) => p)
  }

}

// vim: set ts=4 sw=4 et:

package swords.ecs

import scala.reflect.runtime.universe._

final case class Entity(id: Long) extends AnyVal {
  def update[T : TypeTag](newValue: T): Update[T] =
    Update[T](typeTag[T], this, newValue)
}

object Entity {
  private var nextId: Long = 0

  def create(): Entity = {
    val ref = new Entity(nextId)
    nextId += 1
    ref
  }
}



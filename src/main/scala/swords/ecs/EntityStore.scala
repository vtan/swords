package swords.ecs

import scala.reflect.runtime.universe._

class EntityStore private(
  private val components: Map[TypeTag[_], Map[Entity, Any]]
) {

  def add[T : TypeTag](ref: Entity, component: T): EntityStore = {
    val tt = typeTag[T]
    val valuesByEntity = components.getOrElse(tt, Map.empty)
    new EntityStore(
      components.updated(tt, valuesByEntity.updated(ref, component))
    )
  }

  def get[T : TypeTag]: Seq[(Entity, T)] =
    components.getOrElse(typeTag[T], Map.empty)
      .toSeq
      .map { case (ref, component) => (ref, component.asInstanceOf[T]) }

  def get[T : TypeTag, U: TypeTag]: Seq[(Entity, T, U)] = {
    val ts = components.getOrElse(typeTag[T], Map.empty)
    val us = components.getOrElse(typeTag[U], Map.empty)
    (ts.keys ++ us.keys)
      .map { ref =>
        for {
          t <- ts.get(ref).map(_.asInstanceOf[T])
          u <- us.get(ref).map(_.asInstanceOf[U])
        } yield (ref, t, u)
      }
      .collect { case Some(x) => x }
      .toSeq
  }

  def applyChange(change: EntityChange[Any]): EntityStore = {
    val newComponents = change match {
      case Update(tt, entity, newValue) =>
        val valuesByEntity = components.getOrElse(tt, Map.empty)
        components.updated(tt, valuesByEntity.updated(entity, newValue))
    }
    new EntityStore(newComponents)
  }

  def applySystem(system: System[Any]): EntityStore = {
    val changes = system(this)
    changes.foldLeft(this)(_.applyChange(_))
  }

  override def toString: String = components.toString

}

object EntityStore {
  def apply(): EntityStore =
    new EntityStore(Map.empty)
}


package swords.ecs

import scala.reflect.runtime.universe._

import shapeless._
import shapeless.ops.hlist.Tupler

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

  def get[L <: HList : Queryable](implicit tupler: Tupler[Entity :: L]): Seq[tupler.Out] =
    implicitly[Queryable[L]].query(components).map(tupler(_))

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

trait Queryable[T <: HList] {
  def query(components: Map[TypeTag[_], Map[Entity, Any]]): Seq[Entity :: T]
}

object Queryable {
  implicit def singletonQueryable[T : TypeTag]: Queryable[T :: HNil] =
    _.getOrElse(typeTag[T], Map.empty)
      .toSeq
      .map { case (ref, component) => ref :: component.asInstanceOf[T] :: HNil }

  implicit def consQueryable[T : TypeTag, L <: HList : Queryable]: Queryable[T :: L] =
    { components: Map[TypeTag[_], Map[Entity, Any]] =>
      for {
        entity :: tail <- implicitly[Queryable[L]].query(components)
        component <- components.get(typeTag[T]).flatMap(_.get(entity)).toSeq
      } yield entity :: component.asInstanceOf[T] :: tail
    }
}

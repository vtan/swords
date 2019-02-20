package swords.ecs

import scala.reflect.runtime.universe._

sealed trait EntityChange[+T]

final case class Update[+T](
  tt: TypeTag[_],
  entity: Entity,
  newValue: T
) extends EntityChange[T]

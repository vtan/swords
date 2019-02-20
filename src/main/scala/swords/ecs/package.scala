package swords

package object ecs {

  type System[T] = EntityStore => Seq[EntityChange[T]]

}

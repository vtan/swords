package swords

package object components {

  case object Player

  final case class Position(vector: V2[Int])

  final case class Sprite(resourceName: String)

}

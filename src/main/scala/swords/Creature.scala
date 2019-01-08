package swords

final case class Creature(
  name: String,
  position: V2[Int],
  hitPoints: Int,
  attack: Int,
  defense: Int
)

package swords

final case class Creature(
  position: V2[Int],
  hitPoints: Int,
  attack: Int,
  defense: Int
)

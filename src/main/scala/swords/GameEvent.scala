package swords

sealed trait GameEvent

final case class CreatureAttacked(
  attackerName: String,
  defenderName: String,
  damage: Option[Int]
) extends GameEvent

final case class CreatureDied(name: String) extends GameEvent
package swords

sealed trait GameEvent

final case class CreatureAttacked(
  attackerName: String,
  defenderName: String,
  damage: Option[Double]
) extends GameEvent

final case class CreatureDied(name: String) extends GameEvent
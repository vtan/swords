package swords

sealed trait GameEvent

final case class DamageDealt(
  attackerName: String,
  defenderName: String,
  damage: Double
) extends GameEvent

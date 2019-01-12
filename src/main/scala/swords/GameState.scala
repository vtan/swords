package swords

final case class GameState(
  player: Creature,
  enemies: Vector[Creature],
  events: Vector[GameEvent],
) {
  lazy val gameOver: Boolean = player.hitPoints <= 0

  def replaceEnemy(enemy: Creature, newEnemy: Option[Creature]): GameState =
    newEnemy match {
      case Some(ne) => copy(enemies = enemies.map(e => if (e == enemy) ne else e))
      case None => copy(enemies = enemies.filterNot(_ == enemy))
    }

  def appendEvents(newEvents: Vector[GameEvent]): GameState =
    copy(events = events ++ newEvents)
}

object GameState {
  val initial = GameState(
    player = Creature(
      name = "Player",
      position = V2(10, 10),
      hitPoints = 12,
      attack = 2,
      defense = 14,
      hasAdvantage = false
    ),
    enemies = Vector(
      Creature(
        name = "Enemy 1",
        position = V2(3, 3),
        hitPoints = 7,
        attack = 1,
        defense = 12,
        hasAdvantage = false
      ),
      Creature(
        name = "Enemy 2",
        position = V2(12, 2),
        hitPoints = 7,
        attack = 1,
        defense = 12,
        hasAdvantage = false
      )
    ),
    events = Vector.empty
  )
}

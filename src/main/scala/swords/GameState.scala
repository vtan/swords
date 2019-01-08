package swords

final case class GameState(
  player: Creature,
  enemies: Vector[Creature],
  events: Vector[GameEvent],
) {
  lazy val gameOver: Boolean = player.hitPoints <= 0

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
      defense = 2,
    ),
    enemies = Vector(
      Creature(
        name = "Enemy 1",
        position = V2(3, 3),
        hitPoints = 7,
        attack = 1,
        defense = 1
      ),
      Creature(
        name = "Enemy 2",
        position = V2(12, 2),
        hitPoints = 7,
        attack = 1,
        defense = 1
      )
    ),
    events = Vector.empty
  )
}

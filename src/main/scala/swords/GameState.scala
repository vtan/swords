package swords

final case class GameState(
  player: Creature,
  enemies: Vector[Creature],
  events: Vector[GameEvent],
  gameOver: Boolean
)

object GameState {
  val initial = GameState(
    player = Creature(
      position = V2(10, 10),
      hitPoints = 12,
      attack = 2,
      defense = 2,
    ),
    enemies = Vector(
      Creature(
        position = V2(3, 3),
        hitPoints = 7,
        attack = 1,
        defense = 1
      ),
      Creature(
        position = V2(12, 2),
        hitPoints = 7,
        attack = 1,
        defense = 1
      )
    ),
    events = Vector.empty,
    gameOver = false
  )
}

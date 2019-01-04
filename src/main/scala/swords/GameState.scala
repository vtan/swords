package swords

final case class GameState(
  player: Creature,
  enemies: Vector[Creature],
  gameOver: Boolean
)

object GameState {
  val initial = GameState(
    player = Creature(
      position = V2(10, 10),
      hitPoints = 12
    ),
    enemies = Vector(
      Creature(
        position = V2(3, 3),
        hitPoints = 7
      ),
      Creature(
        position = V2(12, 2),
        hitPoints = 7
      )
    ),
    gameOver = false
  )
}

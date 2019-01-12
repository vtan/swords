package swords

final case class GameState(
  player: Creature,
  enemies: Vector[Creature],
  events: Vector[GameEvent],
) {
  lazy val gameOver: Boolean = player.hitPoints <= 0

  def isTileEmpty(pos: V2[Int]): Boolean =
    player.position != pos && enemies.forall(_.position != pos)

  def emptyTilesNextTo(pos: V2[Int]): Seq[V2[Int]] =
    for {
      dx <- Seq(-1, 0, 1)
      dy <- Seq(-1, 0, 1)
      if dx != 0 || dy != 0
      neighbor = pos + V2(dx, dy)
      if isTileEmpty(neighbor)
    } yield neighbor

  def updateEnemy(enemy: Creature)(f: PartialFunction[Creature, Creature]): GameState = {
    val leaveOthers: PartialFunction[Creature, Creature] = { case e if e != enemy => e }
    copy(enemies = enemies.collect(leaveOthers orElse f))
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

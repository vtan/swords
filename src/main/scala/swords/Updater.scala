package swords

import java.awt.event.KeyEvent

object Updater {

  def update(keyEvent: KeyEvent, gs: GameState): GameState = {
    direction(keyEvent) match {
      case Some(dir) =>
        val target = gs.player.position + dir
        val attackedEnemy = gs.enemies.find(_.position == target)
        attackedEnemy match {
          case Some(enemy) =>
            attack(enemy, gs)
          case None =>
            gs.copy(player = gs.player.copy(position = target))
        }
      case None =>
        gs
    }
  }

  private def attack(enemy: Creature, gs: GameState): GameState = {
    val damagedPlayer = gs.player.copy(hitPoints = gs.player.hitPoints - damage)
    val damagedEnemy = enemy.copy(hitPoints = enemy.hitPoints - damage)
    gs.copy(
      player = damagedPlayer,
      enemies =
        if (damagedEnemy.hitPoints >= 0) {
          gs.enemies.map(e => if (e == enemy) damagedEnemy else e)
        } else {
          gs.enemies.filterNot(_ == enemy)
        },
      gameOver = damagedPlayer.hitPoints <= 0
    )
  }

  private def direction(keyEvent: KeyEvent): Option[V2[Int]] =
    keyEvent.getKeyCode match {
      case KeyEvent.VK_LEFT => Some(V2(-1, 0))
      case KeyEvent.VK_RIGHT => Some(V2(1, 0))
      case KeyEvent.VK_UP => Some(V2(0, -1))
      case KeyEvent.VK_DOWN => Some(V2(0, 1))
      case _ => None
    }

  private val damage: Int = 2

}

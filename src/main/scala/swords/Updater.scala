package swords

import scala.util.Random
import java.awt.event.KeyEvent

import com.softwaremill.quicklens._

object Updater {

  private val random = new Random()

  def update(keyEvent: KeyEvent, gs: GameState): GameState = {
    direction(keyEvent) match {
      case Some(dir) =>
        val target = gs.player.position + dir
        val attackedEnemy = gs.enemies.find(_.position == target)
        attackedEnemy match {
          case Some(enemy) =>
            attack(enemy, gs)
          case None =>
            gs.modify(_.player.position).setTo(target)
        }
      case None =>
        gs
    }
  }

  private def attack(enemy: Creature, gs: GameState): GameState = {
    val enemyDamage = resolveDamage(enemy, gs.player)
    val damagedPlayer = gs.player.modify(_.hitPoints).using(_ - enemyDamage.getOrElse(0.0))

    val playerDamage = resolveDamage(gs.player, enemy)
    val damagedEnemy = enemy.modify(_.hitPoints).using(_ - playerDamage.getOrElse(0.0))

    val newEvents = Vector(
      CreatureAttacked("enemy", "player", enemyDamage),
      CreatureAttacked("player", "enemy", playerDamage)
    ) ++
      (if (damagedEnemy.hitPoints < 0) Vector(CreatureDied("enemy")) else Vector.empty) ++
      (if (damagedPlayer.hitPoints < 0) Vector(CreatureDied("player")) else Vector.empty)

    gs.copy(
      player = damagedPlayer,
      enemies =
        if (damagedEnemy.hitPoints >= 0) {
          gs.enemies.map(e => if (e == enemy) damagedEnemy else e)
        } else {
          gs.enemies.filterNot(_ == enemy)
        },
      events = gs.events ++ newEvents,
      gameOver = damagedPlayer.hitPoints <= 0
    )
  }

  private def resolveDamage(attacker: Creature, defender: Creature): Option[Double] = {
    val roll = random.nextGaussian()
    val damage = roll + attacker.attack - defender.defense
    Some(damage).filter(_ > 0)
  }

  private def direction(keyEvent: KeyEvent): Option[V2[Int]] =
    keyEvent.getKeyCode match {
      case KeyEvent.VK_LEFT => Some(V2(-1, 0))
      case KeyEvent.VK_RIGHT => Some(V2(1, 0))
      case KeyEvent.VK_UP => Some(V2(0, -1))
      case KeyEvent.VK_DOWN => Some(V2(0, 1))
      case _ => None
    }

}

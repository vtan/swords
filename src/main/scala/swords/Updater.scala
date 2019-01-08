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
        val gsAfterPlayerAction = attackedEnemy match {
          case Some(enemy) =>
            attackEnemy(enemy, gs)
          case None =>
            gs.modify(_.player.position).setTo(target)
        }
        stepEnemies(gsAfterPlayerAction)
      case None =>
        gs
    }
  }

  private def stepEnemies(gs: GameState): GameState =
    gs.enemies.foldLeft(gs) { case (gsAcc, enemy) => stepEnemy(enemy, gsAcc) }

  private def stepEnemy(enemy: Creature, gs: GameState): GameState = {
    val toPlayer = gs.player.position - enemy.position
    val nextToPlayer = toPlayer.map(_.abs).max == 1
    if (nextToPlayer) {
      val (damagedPlayer, events) = attack(enemy, gs.player)
      gs
        .copy(player = damagedPlayer)
        .appendEvents(events)
    } else {
      val direction = toPlayer.map(_.signum)
      val target = enemy.position + direction
      if (gs.enemies.exists(_.position == target)) {
        gs
      } else {
        val movedEnemy = enemy.modify(_.position).using(_ + direction)
        gs.modify(_.enemies).using(_.map(e => if (e == enemy) movedEnemy else e))
      }
    }
  }

  private def attackEnemy(enemy: Creature, gs: GameState): GameState = {
    val (damagedEnemy, events) = attack(gs.player, enemy)
    gs
      .modify(_.enemies).using { enemies =>
        if (damagedEnemy.hitPoints > 0) {
          enemies.map(e => if (e == enemy) damagedEnemy else e)
        } else {
          enemies.filterNot(_ == enemy)
        }
      }
      .appendEvents(events)
  }

  private def attack(attacker: Creature, defender: Creature): (Creature, Vector[GameEvent]) = {
    val damage = resolveDamage(attacker, defender)
    val damagedDefender = defender.modify(_.hitPoints).using(_ - damage.getOrElse(0))
    val events = CreatureAttacked(attacker.name, defender.name, damage) +:
      (if (damagedDefender.hitPoints <= 0) Vector(CreatureDied(defender.name)) else Vector.empty)
    (damagedDefender, events)
  }

  private def resolveDamage(attacker: Creature, defender: Creature): Option[Int] = {
    val roll = (1 to 4).map(_ => random.nextInt(3) - 1).sum
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

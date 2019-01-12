package swords

import scala.util.Random

import com.softwaremill.quicklens._
import scalafx.scene.input.{KeyCode, KeyEvent}

object Updater {

  private val random = new Random()

  def update(keyEvent: KeyEvent, gs: GameState): GameState = {
    if (gs.gameOver) {
      GameState.initial
    } else {
      direction(keyEvent) match {
        case Some(dir) =>
          val target = gs.player.position + dir
          val attackedEnemy = gs.enemies.find(_.position == target)
          val gsAfterPlayerAction = attackedEnemy match {
            case Some(enemy) =>
              val (newPlayer, newEnemy, events) = attack(gs.player, enemy)
              gs
                .copy(player = newPlayer)
                .updateEnemy(enemy){ case _ if newEnemy.hitPoints > 0 => newEnemy }
                .appendEvents(events)
            case None =>
              gs.modify(_.player.position).setTo(target)
          }
          stepEnemies(gsAfterPlayerAction)
        case None =>
          gs
      }
    }
  }

  private def stepEnemies(gs: GameState): GameState =
    gs.enemies.foldLeft(gs) { case (gsAcc, enemy) => stepEnemy(enemy, gsAcc) }

  private def stepEnemy(enemy: Creature, gs: GameState): GameState = {
    val toPlayer = gs.player.position - enemy.position
    val nextToPlayer = toPlayer.map(_.abs).max == 1
    if (nextToPlayer) {
      val (newEnemy, newPlayer, events) = attack(enemy, gs.player)
      gs
        .copy(player = newPlayer)
        .updateEnemy(enemy){ case _ if newEnemy.hitPoints > 0 => newEnemy }
        .appendEvents(events)
    } else {
      val direction = toPlayer.map(_.signum)
      val target = enemy.position + direction
      if (gs.isTileEmpty(target)) {
        val movedEnemy = enemy.modify(_.position).using(_ + direction)
        gs.modify(_.enemies).using(_.map(e => if (e == enemy) movedEnemy else e))
      } else {
        val neighborClosestToPlayer = gs
          .emptyTilesNextTo(enemy.position)
          .sortBy(_.maxDistance(gs.player.position))
          .headOption
        neighborClosestToPlayer match {
          case Some(pos) => gs.updateEnemy(enemy){ case _ => enemy.copy(position = pos) }
          case None => gs
        }
      }
    }
  }

  private def attack(attacker: Creature, defender: Creature): (Creature, Creature, Vector[GameEvent]) = {
    val attackResult = resolveAttack(attacker, defender)
    val newAttacker = attacker.copy(hasAdvantage = attackResult.advantage)
    val newDefender = defender.modify(_.hitPoints).using(_ - attackResult.damage.getOrElse(0))
    val events = Vector.concat(
      Vector(CreatureAttacked(attacker.name, defender.name, attackResult.damage)),
      if (newDefender.hitPoints <= 0) Vector(CreatureDied(defender.name)) else Vector.empty,
      if (attackResult.advantage) Vector(CreatureGainedAdvantage(attacker.name)) else Vector.empty
    )
    (newAttacker, newDefender, events)
  }

  private def resolveAttack(attacker: Creature, defender: Creature): AttackResult = {
    val attackRoll = (1 to 3).map(_ => random.nextInt(6) + 1).sum
    val attackBonus = attacker.attack + (if (attacker.hasAdvantage) 2 else 0)
    val damage = attackRoll + attackBonus - defender.defense
    damage match {
      case 0 => AttackResult(damage = None, advantage = true)
      case _ if damage > 0 => AttackResult(damage = Some(damage), advantage = false)
      case _ => AttackResult(damage = None, advantage = false)
    }
  }

  private def direction(keyEvent: KeyEvent): Option[V2[Int]] =
    (keyEvent.shiftDown, keyEvent.code) match {
      case (false, KeyCode.Left) => Some(V2(-1, 0))
      case (false, KeyCode.Right) => Some(V2(1, 0))
      case (false, KeyCode.Up) => Some(V2(0, -1))
      case (false, KeyCode.Down) => Some(V2(0, 1))
      case (true, KeyCode.Left) => Some(V2(-1, -1))
      case (true, KeyCode.Right) => Some(V2(1, 1))
      case (true, KeyCode.Up) => Some(V2(1, -1))
      case (true, KeyCode.Down) => Some(V2(-1, 1))
      case _ => None
    }

}

private final case class AttackResult(
  damage: Option[Int],
  advantage: Boolean
)
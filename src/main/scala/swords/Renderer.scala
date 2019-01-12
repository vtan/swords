package swords

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color

object Renderer {

  def render(graphics: GraphicsContext, screenSize: V2[Double])(gs: GameState): Unit = {
    graphics.fill = Color.Black
    graphics.fillRect(0, 0, screenSize.x, screenSize.y)

    renderCreature(graphics)(playerColor, gs.player)
    gs.enemies.foreach(renderCreature(graphics)(enemyColor, _))

    (Stream.from(0) zip gs.events.takeRight(10).reverse).foreach {
      case (i, event) =>
        val message = event match {
          case CreatureAttacked(attackerName, defenderName, Some(damage)) =>
            s"$attackerName dealt $damage damage to $defenderName"
          case CreatureAttacked(attackerName, defenderName, None) =>
            s"$attackerName attacked $defenderName but missed"
          case CreatureDied(name) =>
            s"$name died"
          case CreatureGainedAdvantage(name) =>
            s"$name gained an advantage"
        }
        graphics.fill = Color.White
        graphics.fillText(message, 0, screenSize.y - i * 16)
    }

    if (gs.gameOver) {
      graphics.fill = Color.White
      graphics.fillText("You died. Press any key to try again.", 0, 16)
    }
  }

  private def renderCreature(graphics: GraphicsContext)(color: Color, creature: Creature): Unit = {
    val posPx = creature.position * tileSize
    graphics.fill = if (creature.hasAdvantage) color.brighter else color
    graphics.fillRect(posPx.x, posPx.y, tileSize.x, tileSize.y)
    graphics.fill = Color.White
    graphics.fillText(s"HP: ${creature.hitPoints}", posPx.x, posPx.y + tileSize.y)
  }

  private val tileSize: V2[Int] = V2(48, 48)
  private val playerColor = Color(0, 0.5, 1, 1)
  private val enemyColor = Color(1, 0.5, 0, 1)
}

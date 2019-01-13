package swords

import scalafx.scene.effect.ColorAdjust
import scalafx.scene.paint.Color

object Renderer {

  def render(gs: GameState)(implicit renderEnv: RenderEnv): Unit = {
    val graphics = renderEnv.graphicsContext

    graphics.fill = Color.Black
    graphics.fillRect(0, 0, renderEnv.screenSize.x, renderEnv.screenSize.y)

    renderCreature(gs.player)
    gs.enemies.foreach(renderCreature)

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
        graphics.fillText(message, 0, renderEnv.screenSize.y - i * 16)
    }

    if (gs.gameOver) {
      graphics.fill = Color.White
      graphics.fillText("You died. Press any key to try again.", 0, 16)
    }
  }

  private def renderCreature(creature: Creature)(implicit renderEnv: RenderEnv): Unit = {
    val graphics = renderEnv.graphicsContext

    val posPx = creature.position * tileSize
    val (image, color) = if (creature.name == "Player") {
      ("fighter", Color(0, 0.5, 1, 1))
    } else {
      ("goblin", Color(0.5, 1, 0.5, 1))
    }
    graphics.setEffect(colorAdjust(color))
    graphics.drawImage(renderEnv.resources.tile(image), posPx.x, posPx.y, tileSize.x, tileSize.y)
    graphics.setEffect(null)

    graphics.fill = Color.White
    graphics.fillText(s"HP: ${creature.hitPoints}", posPx.x, posPx.y + tileSize.y + 16)
  }

  private def colorAdjust(color: Color) = new ColorAdjust(
    hue = ((color.hue + 180) % 360 - 180) / 180.0,
    saturation = color.saturation,
    brightness = color.brightness - 1,
    contrast = 0
  )

  private val tileSize: V2[Int] = V2(48, 48)
}

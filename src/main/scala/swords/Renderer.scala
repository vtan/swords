package swords

import java.awt.{Color, Graphics, Graphics2D}
import javax.swing.JPanel

final class Renderer(
  screenSize: V2[Int],
  var gs: GameState
) extends JPanel {

  override def paintComponent(graphics: Graphics): Unit = {
    super.paintComponent(graphics)

    graphics.setColor(new Color(0, 0, 0))
    graphics.fillRect(0, 0, screenSize.x, screenSize.y)

    Renderer.render(graphics.asInstanceOf[Graphics2D], gs)
  }

}

object Renderer {

  def render(graphics: Graphics2D, gs: GameState): Unit = {
    renderCreature(graphics, playerColor, gs.player)
    gs.enemies.foreach(renderCreature(graphics, enemyColor, _))

    if (gs.gameOver) {
      graphics.setColor(Color.white)
      graphics.drawString("You died. Press any key to try again.", 0, 16)
    }
  }

  private def renderCreature(graphics: Graphics2D, color: Color, creature: Creature): Unit = {
    val posPx = creature.position * tileSize
    graphics.setColor(color)
    graphics.fillRect(posPx.x, posPx.y, tileSize.x, tileSize.y)
    graphics.setColor(Color.white)
    graphics.drawString("HP: " + creature.hitPoints, posPx.x, posPx.y)
  }

  private val tileSize: V2[Int] = V2(32, 32)
  private val playerColor = new Color(0, 127, 255)
  private val enemyColor = new Color(255, 127, 0)
}
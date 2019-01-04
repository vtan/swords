package swords

import java.awt.{Color, Graphics, Graphics2D}
import javax.swing.JPanel

class Renderer(
  screenSize: V2[Int],
  var gs: GameState
) extends JPanel {

  override def paintComponent(graphics: Graphics): Unit = {
    super.paintComponent(graphics)
    Renderer.render(gs, screenSize, graphics.asInstanceOf[Graphics2D])
  }

}

object Renderer {

  def render(gs: GameState, screenSize: V2[Int], graphics: Graphics2D): Unit = {
    graphics.setColor(new Color(0, 0, 0))
    graphics.fillRect(0, 0, screenSize.x, screenSize.y)

    val playerPx = gs.playerPosition * tileSize
    graphics.setColor(new Color(0, 127, 255))
    graphics.fillRect(playerPx.x, playerPx.y, tileSize.x, tileSize.y)
  }

  private val tileSize: V2[Int] = V2(32, 32)
}

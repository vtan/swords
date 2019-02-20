package swords

import scalafx.scene.paint.Color

object Renderer {

  def render(renderables: Seq[Renderable])(implicit renderEnv: RenderEnv): Unit = {
    val graphics = renderEnv.graphicsContext

    graphics.fill = Color.Black
    graphics.fillRect(0, 0, renderEnv.screenSize.x, renderEnv.screenSize.y)

    renderables.foreach { renderable =>
      val posPx = renderable.position.vector * tileSize
      val image = renderEnv.resources.tile(renderable.sprite.resourceName)
      graphics.drawImage(image, posPx.x, posPx.y, tileSize.x, tileSize.y)
    }
  }

  private val tileSize: V2[Int] = V2(48, 48)
}

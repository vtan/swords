package swords

import scalafx.scene.canvas.GraphicsContext

case class RenderEnv(
  graphicsContext: GraphicsContext,
  screenSize: V2[Double],
  resources: Resources
)

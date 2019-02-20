package swords

import scala.util.Random

import scalafx.scene.input.{KeyCode, KeyEvent}

object Controller {

  private val random = new Random()

  def update(keyEvent: KeyEvent, entityStore: Unit)(implicit renderEnv: RenderEnv): Unit = {
    val renderables = Seq()
    Renderer.render(renderables)
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

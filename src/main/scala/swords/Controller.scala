package swords

import swords.ecs.EntityStore

import scala.util.Random

import scalafx.scene.input.{KeyCode, KeyEvent}

object Controller {

  private val random = new Random()

  def initialize(implicit renderEnv: RenderEnv): EntityStore = {
    Renderer.render(Seq.empty)
    EntityStore()
  }

  def update(keyEvent: KeyEvent, entityStore: EntityStore)(implicit renderEnv: RenderEnv): EntityStore = {
    val renderables = Seq()
    Renderer.render(renderables)
    entityStore
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

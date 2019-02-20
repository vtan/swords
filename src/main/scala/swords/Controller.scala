package swords

import swords.components.{Player, Position, Sprite}
import swords.ecs.{Entity, EntityStore}

import scala.util.Random

import scalafx.scene.input.{KeyCode, KeyEvent}

object Controller {

  private val random = new Random()

  def initialize(implicit renderEnv: RenderEnv): EntityStore = {
    val player = Entity.create()
    val enemy = Entity.create()

    val es = EntityStore()
      .add(player, Player)
      .add(player, Sprite("fighter"))
      .add(player, Position(V2(5, 5)))
      .add(enemy, Sprite("goblin"))
      .add(enemy, Position(V2(8, 5)))

    val renderables = collectRenderables(es)
    Renderer.render(renderables)

    es
  }

  def update(keyEvent: KeyEvent, es: EntityStore)(implicit renderEnv: RenderEnv): EntityStore = {
    val systemsToRun = direction(keyEvent).map(systems.movePlayer).toSeq

    val updated = systemsToRun.foldLeft(es)(_.applySystem(_))
    Renderer.render(collectRenderables(updated))
    updated
  }

  private def collectRenderables(es: EntityStore): Seq[Renderable] =
    es.get[Sprite, Position].map {
      case (_, sprite, position) => Renderable(position, sprite)
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

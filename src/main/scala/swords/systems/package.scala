package swords

import swords.components.{Player, Position}
import swords.ecs.{EntityStore, System}

import shapeless.{::, HNil}

package object systems {

  def movePlayer(direction: V2[Int]): System[Position] = { es: EntityStore =>
    for {
      player :: _ :: Position(pos) :: HNil <- es.get[Player.type :: Position :: HNil]
      newPos = pos + direction
      neighbor = es.get[Position :: HNil].find { case entity :: neighborPos :: HNil => neighborPos.vector == newPos }
      changes <- neighbor match {
        case None => Seq(player.update(Position(pos + direction)))
        case _ => Seq.empty
      }
    } yield changes
  }

}

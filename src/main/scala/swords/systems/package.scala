package swords

import swords.components.{Player, Position}
import swords.ecs.{EntityStore, System}

package object systems {

  def movePlayer(direction: V2[Int]): System[Position] = { es: EntityStore =>
    for {
      (player, _, Position(pos)) <- es.get[Player.type, Position]
      newPos = pos + direction
      neighbor = es.get[Position].find { case (entity, neighborPos) => neighborPos.vector == newPos }
      changes <- neighbor match {
        case None => Seq(player.update(Position(pos + direction)))
        case _ => Seq.empty
      }
    } yield changes
  }

}

package swords

import scalafx.scene.image.Image

class Resources {
  val tiles = Map(
    "fighter" -> new Image("fighter.png"),
    "goblin" -> new Image("goblin.png")
  )

  def tile(name: String): Image =
    tiles.getOrElse(name, throw new Exception(s"Missing tile: $name"))
}

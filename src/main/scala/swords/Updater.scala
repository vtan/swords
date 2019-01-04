package swords

import java.awt.event.KeyEvent

object Updater {

  def update(keyEvent: KeyEvent, gs: GameState): GameState = {
    keyEvent.getKeyCode match {
      case KeyEvent.VK_LEFT =>
        gs.copy(playerPosition = gs.playerPosition + V2(-1, 0))
      case KeyEvent.VK_RIGHT =>
        gs.copy(playerPosition = gs.playerPosition + V2(1, 0))
      case KeyEvent.VK_UP =>
        gs.copy(playerPosition = gs.playerPosition + V2(0, -1))
      case KeyEvent.VK_DOWN =>
        gs.copy(playerPosition = gs.playerPosition + V2(0, 1))
      case _ =>
        gs
    }
  }

}

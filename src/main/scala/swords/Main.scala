package swords

import java.awt.event.{KeyAdapter, KeyEvent}
import javax.swing.JFrame

object Main {

  def main(args: Array[String]): Unit = {
    var gs = GameState.initial
    val screenSize = V2(1280, 720)
    val renderer = new Renderer(screenSize, gs)

    val frame = new JFrame()
    frame.add(renderer)
    frame.setSize(screenSize.x, screenSize.y)
    frame.setResizable(false)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    frame.addKeyListener {
      new KeyAdapter {
        override def keyPressed(keyEvent: KeyEvent): Unit = {
          gs = if (gs.gameOver) {
            GameState.initial
          } else {
            Updater.update(keyEvent, gs)
          }
          renderer.gs = gs
          frame.repaint()
        }
      }
    }

    frame.setVisible(true)
  }

}
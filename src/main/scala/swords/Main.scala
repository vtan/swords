package swords

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Group, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.KeyEvent

object Main extends JFXApp with scalafx.Includes {

  var gs = GameState.initial
  val screenSize = V2(1280.0, 720.0)

  val canvas = new Canvas(screenSize.x, screenSize.y)
  val graphics = canvas.graphicsContext2D

  private def render(): Unit =
    Renderer.render(graphics, screenSize)(gs)

  stage = new PrimaryStage {
    resizable = false

    scene = new Scene(screenSize.x, screenSize.y) {
      maxWidth = screenSize.x
      maxHeight = screenSize.y
      minWidth = screenSize.x
      minHeight = screenSize.y

      root = new Group {
        children = canvas
      }
      onKeyPressed = (keyEvent: KeyEvent) => {
        gs = Updater.update(keyEvent, gs)
        render()
      }
    }
  }

  render()

}
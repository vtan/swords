package swords

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Group, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.KeyEvent

object Main extends JFXApp with scalafx.Includes {

  val screenSize = V2(1824.0, 960.0)
  val resources = new Resources

  val canvas = new Canvas(screenSize.x, screenSize.y)
  val renderEnv = RenderEnv(canvas.graphicsContext2D, screenSize, resources)

  var entityStore = Controller.initialize(renderEnv)

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
        entityStore = Controller.update(keyEvent, entityStore)(renderEnv)
      }
    }
  }
}
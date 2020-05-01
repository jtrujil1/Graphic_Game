package graphicgame

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.canvas.Canvas
import scala.concurrent.Future
import scalafx.scene.Scene
import scala.concurrent.ExecutionContext.Implicits.global
import scalafx.application.Platform
import scalafx.scene.control.TextField

object TestMain extends JFXApp {

    var playerScore = 300

    stage = new JFXApp.PrimaryStage {
		title = "Adventures of Sergio"
		scene = new Scene(1000, 800) {
            val canvas = new Canvas (1000,800)
	        val gc = canvas.graphicsContext2D
            val renderer = new Renderer2D(gc, 30)
            content = canvas

            val tf = new TextField
            tf.setPromptText(s"<Score: ${playerScore.toString}>")

        }
    }
}
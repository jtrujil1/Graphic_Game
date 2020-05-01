package graphicgame

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.canvas.Canvas
import java.net.Socket
import java.io.ObjectOutputStream
import java.io.ObjectInputStream
import scala.concurrent.Future
import scalafx.scene.Scene
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scala.concurrent.ExecutionContext.Implicits.global
import scalafx.application.Platform

object Client extends JFXApp {
    
    val sock = new Socket("localhost", 8000)
    val oos = new ObjectOutputStream(sock.getOutputStream())
    val ois = new ObjectInputStream(sock.getInputStream())

    var score = 0


    stage = new JFXApp.PrimaryStage {
		title = "Adventures of Sergio"
		scene = new Scene(1000, 800) {
            val canvas = new Canvas (1000,800)
	        val gc = canvas.graphicsContext2D
            val renderer = new Renderer2D(gc, 30)
            content = canvas

            onKeyPressed = (ke: KeyEvent) => {
                oos.writeInt(KeyData.KeyPressed)
                oos.writeInt(KeyData.codeToInt(ke.code))
                oos.flush()
            }
            onKeyReleased = (ke: KeyEvent) => {
                oos.writeInt(KeyData.KeyReleased)
                oos.writeInt(KeyData.codeToInt(ke.code))
                oos.flush()
            }

            Future {
                while(true) {
                    val pl = ois.readObject() match {
                        case l: UpdateInfo => l
                        case _ => println("Error with package sent to client.")
                        null
                    }
                    Platform.runLater(renderer.render(pl.level, pl.px, pl.py, pl.score))
                }
            }
        }
    }
}
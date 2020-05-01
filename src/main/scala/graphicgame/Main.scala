package graphicgame

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import scalafx.animation.AnimationTimer

/**
 * This is a stub for the graphical game.
 */
object Main extends JFXApp {
	val canvas = new Canvas(1000,800)
	val gc = canvas.graphicsContext2D
	val renderer = new Renderer2D(gc, 30)
	val maze = RandomMaze(4, false, 20, 20, 0.6)
	var level1 = new Level(maze, Nil)
	val ghost = new Ghost(58, 58, level1)
	val ghost2 = new Ghost(59, 8, level1)
	val ghost3 = new Ghost(7, 59, level1)
	val ghost4 = new Ghost(43, 13, level1)
	val ghost5 = new Ghost(30, 13, level1)
	var player = new Player(3, 3, level1)
	val demon = new Demon(29, 50, level1)
	val demon2 = new Demon(50, 21, level1)
	player.initialLocation()
	ghost.initialLocation()
	ghost2.initialLocation()
	ghost3.initialLocation()
	ghost4.initialLocation()
	ghost5.initialLocation()
	demon.initialLocation()
	demon2.initialLocation()
	level1 += player
	level1 += ghost
	level1 += ghost2
	level1 += ghost3
	level1 += ghost4
	level1 += ghost5
	level1 += demon
	level1 += demon2

	stage = new JFXApp.PrimaryStage {
		title = "Adventures of Sergio"
		scene = new Scene(1000, 800) {
			content = canvas

			onKeyPressed = (ke: KeyEvent) => player.keyPressed(KeyData.codeToInt(ke.code))
      		onKeyReleased = (ke: KeyEvent) => player.keyReleased(KeyData.codeToInt(ke.code))

			var lastTime = -1L
      		val timer = AnimationTimer(time => {
        		if (lastTime >= 0) {
          			val delay = (time - lastTime)/1e9
					level1.updateAll(delay)
					val pl = level1.buildPassable
					renderer.render(pl, player.x, player.y, 0)
        		}
        		lastTime = time
      		})
			timer.start()
			 
		}
	}
}

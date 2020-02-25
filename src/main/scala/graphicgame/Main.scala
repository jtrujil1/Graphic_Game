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
	val maze = RandomMaze(3, false, 20, 20, 0.6)
	var level1 = new Level(maze, Nil)
	val enemy = new Enemy(59, 59, level1)
	val enemy2 = new Enemy(59, 2, level1)
	val enemy3 = new Enemy(2, 59, level1)
	var player = new Player(2, 2, level1)
	player.initialLocation()
	//enemy.initialLocation()
	level1 += player
	level1 += enemy
	level1 += enemy2
	level1 += enemy3

	stage = new JFXApp.PrimaryStage {
		title = "Adventures of Sergio"
		scene = new Scene(1000, 800) {
			content = canvas

			onKeyPressed = (ke: KeyEvent) => player.keyPressed(ke.code)
      		onKeyReleased = (ke: KeyEvent) => player.keyReleased(ke.code)

			var lastTime = -1L
      		val timer = AnimationTimer(time => {
        		if (lastTime >= 0) {
          			val delay = (time - lastTime)/1e9
					level1.updateAll(delay)
					renderer.render(level1, player.x, player.y)
        		}
        		lastTime = time
      		})
			timer.start()
			 
		}
	}
}

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
	val enemy = new Enemy(58, 58, level1)
	val enemy2 = new Enemy(59, 8, level1)
	val enemy3 = new Enemy(7, 59, level1)
	val enemy4 = new Enemy(43, 13, level1)
	val enemy5 = new Enemy(30, 13, level1)
	var player = new Player(3, 3, level1)
	player.initialLocation()
	enemy.initialLocation()
	enemy2.initialLocation()
	enemy3.initialLocation()
	enemy4.initialLocation()
	enemy5.initialLocation()
	level1 += player
	level1 += enemy
	level1 += enemy2
	level1 += enemy3
	level1 += enemy4
	level1 += enemy5

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
					renderer.render(pl, player.x, player.y)
        		}
        		lastTime = time
      		})
			timer.start()
			 
		}
	}
}

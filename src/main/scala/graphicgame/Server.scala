package graphicgame

import scalafx.Includes._
import java.net.ServerSocket
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import scala.collection.mutable
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

case class PlayerConnection(player: Player, sock: Socket, in: ObjectInputStream, out: ObjectOutputStream)

object Server extends App {
    val connections = mutable.Buffer[PlayerConnection]()
    val queue = new LinkedBlockingQueue[PlayerConnection]()

    val maze = RandomMaze(4, false, 20, 20, 0.6)
	var level = new Level(maze, Nil)
	val enemy = new Enemy(58, 58, level)
	val enemy2 = new Enemy(59, 8, level)
	val enemy3 = new Enemy(7, 59, level)
	val enemy4 = new Enemy(43, 13, level)
	val enemy5 = new Enemy(30, 13, level)
	enemy.initialLocation()
	enemy2.initialLocation()
	enemy3.initialLocation()
	enemy4.initialLocation()
	enemy5.initialLocation()
	level += enemy
	level += enemy2
	level += enemy3
	level += enemy4
	level += enemy5

    val ss = new ServerSocket(8000)
    Future {
        while(true) {
            val sock = ss.accept()
            val ois = new ObjectInputStream(sock.getInputStream())
            val oos = new ObjectOutputStream(sock.getOutputStream())
            val player = new Player(3, 3, level)
            player.initialLocation()
            level += player
            queue.put(PlayerConnection(player, sock, ois, oos))
        }
    }

    var lastTime = -1L
    var updateTimer = 0.0
    while(true) {
        while (!queue.isEmpty()) {
            connections += queue.take()
        }

        val time = System.nanoTime()
        if (lastTime >= 0) {
            val delay = (time - lastTime)/1e9
            level.updateAll(delay)
            updateTimer += delay
            var sendUpdate = false
            if(updateTimer >= 1/30.0){
                sendUpdate = true
                updateTimer = 0.0
            }
            for (pc <- connections) {
                if (pc.in.available() > 0) {
                    pc.in.readInt() match {
                        case KeyData.KeyPressed => pc.player.keyPressed(pc.in.readInt())
                        case KeyData.KeyReleased => pc.player.keyReleased(pc.in.readInt())
                        case x => println(s"Unhandled $x input.")
                    }
                }

                if (sendUpdate) {
                    pc.out.writeObject(new UpdateInfo(level.buildPassable, pc.player.x, pc.player.y))
                    pc.out.flush()
                }
            }
        }
        lastTime = time
    }   
}
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
	val ghost = new Ghost(58, 58, level)
	val ghost2 = new Ghost(59, 8, level)
	val ghost3 = new Ghost(7, 59, level)
	val ghost4 = new Ghost(43, 13, level)
    val demon = new Demon(29, 50, level)
    val demon2 = new Demon(50, 21, level)
    val demon3 = new Demon(30, 13, level)
	ghost.initialLocation()
	ghost2.initialLocation()
	ghost3.initialLocation()
	ghost4.initialLocation()
    demon.initialLocation()
    demon2.initialLocation()
    demon3.initialLocation()
	level += ghost
	level += ghost2
	level += ghost3
	level += ghost4
    level += demon
    level += demon2
    level += demon3

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
                    pc.out.writeObject(new UpdateInfo(level.buildPassable, pc.player.x, pc.player.y, pc.player.score))
                    pc.out.flush()
                }
            }
        }
        lastTime = time
    }   
}
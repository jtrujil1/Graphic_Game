package graphicgame

import scalafx.scene.input.KeyCode

class Player (private var _x: Double, private var _y: Double, val level: Level) extends Entity {
    def currentPlayer = this

    def x: Double = _x
    def y: Double = _y
    def width: Double = 1.1
    def height: Double = 1.3

    def initialLocation(): Unit = {
        while(!(moveAllowed(_x, _y))){
            _x += 0.3
            _y += 0.3
        }
    }

    private var keysHeld = Set[KeyCode]()

    def keyPressed(keyCode: KeyCode): Unit = keysHeld += keyCode
    def keyReleased(keyCode: KeyCode): Unit = keysHeld -= keyCode

    def move(dx: Double, dy: Double, moveAllowed: Boolean): Unit = {
        if(moveAllowed){
            _x += dx
            _y += dy
        }
    }

    def moveAllowed(x:Double, y:Double): Boolean = {
        (level.maze.isClear(x, y, width, height, currentPlayer) && intersects)
    }

    def intersects: Boolean = {
        for(i <- 0 until level.entities.length){
            if(Entity.intersect(this, level.entities(i)) && level.entities(i) != this){
                return false
            }
        }
        return true
    }

    def update(delay: Double): Unit = {
        if (keysHeld(KeyCode.Up)) move(0, -0.2, moveAllowed(_x, _y - 0.2))
        if (keysHeld(KeyCode.Down)) move(0, 0.2, moveAllowed(_x, _y + 0.2))
        if (keysHeld(KeyCode.Left)) move(-0.2, 0, moveAllowed(_x - 0.2, _y))
        if (keysHeld(KeyCode.Right)) move(0.2, 0, moveAllowed(_x + 0.2, _y))
        if (keysHeld(KeyCode.W)){
            var bullet = new Bullet(_x, _y - 0.2, level, "u")
            level += bullet
        }
        if (keysHeld(KeyCode.A)){
            var bullet = new Bullet(_x - 0.2, _y, level, "l")
            level += bullet
        }
        if (keysHeld(KeyCode.S)){
            var bullet = new Bullet(_x, _y + 0.2, level, "d")
            level += bullet
        }
        if (keysHeld(KeyCode.D)){
            var bullet = new Bullet(_x + 0.2, _y, level, "r")
            level += bullet
        }
    }
    def postCheck(): Unit = ???// You can delete this if you don't use it.
    def stillHere(): Boolean = true// This is how you should remove entites from the level.

}
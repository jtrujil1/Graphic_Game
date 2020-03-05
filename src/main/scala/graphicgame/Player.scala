package graphicgame

import scalafx.scene.input.KeyCode

class Player (private var _x: Double, private var _y: Double, val level: Level) extends Entity {
    def currentPlayer = this

    def x: Double = _x
    def y: Double = _y
    def width: Double = 1.3
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

    var moveDelay = 0.0
    var moveInterval = 0.08

    def update(delay: Double): Unit = {
        var speed = 8
        moveDelay += delay
        if (keysHeld(KeyCode.Up)) move(0, -speed*delay, moveAllowed(_x, _y - speed*delay))
        if (keysHeld(KeyCode.Down)) move(0, speed*delay, moveAllowed(_x, _y + speed*delay))
        if (keysHeld(KeyCode.Left)) move(-speed*delay, 0, moveAllowed(_x - speed*delay, _y))
        if (keysHeld(KeyCode.Right)) move(speed*delay, 0, moveAllowed(_x + speed*delay, _y))
        if (keysHeld(KeyCode.W)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x, _y - 1, level, "u")
                level += bullet
                moveDelay = 0.0
            }
        }
        if (keysHeld(KeyCode.A)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x - 0.5, _y, level, "l")
                level += bullet
                moveDelay = 0.0
            }
        }
        if (keysHeld(KeyCode.S)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x, _y + 0.5, level, "d")
                level += bullet
                moveDelay = 0.0
            }
        }
        if (keysHeld(KeyCode.D)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x + 0.5, _y, level, "r")
                level += bullet
                moveDelay = 0.0
            }
        }
    }

    def stillHere(): Boolean = {
        var ret = true
        if(level.enemies.length > 0){
            for(i <- 0 until level.enemies.length){
                if(Entity.intersect(this, level.enemies(i))){
                    ret = false
                }
            }
        }
        return ret
    }

}
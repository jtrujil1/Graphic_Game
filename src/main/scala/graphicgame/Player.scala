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

    private var keysHeld = Set[Int]()

    def keyPressed(keyCode: Int): Unit = keysHeld += keyCode
    def keyReleased(keyCode: Int): Unit = keysHeld -= keyCode

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
        var speed = 9
        moveDelay += delay
        if (keysHeld(KeyData.Up)) move(0, -speed*delay, moveAllowed(_x, _y - speed*delay))
        if (keysHeld(KeyData.Down)) move(0, speed*delay, moveAllowed(_x, _y + speed*delay))
        if (keysHeld(KeyData.Left)) move(-speed*delay, 0, moveAllowed(_x - speed*delay, _y))
        if (keysHeld(KeyData.Right)) move(speed*delay, 0, moveAllowed(_x + speed*delay, _y))
        if (keysHeld(KeyData.W)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x, _y - 1, level, "u")
                level += bullet
                moveDelay = 0.0
            }
        }
        if (keysHeld(KeyData.A)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x - 1, _y, level, "l")
                level += bullet
                moveDelay = 0.0
            }
        }
        if (keysHeld(KeyData.S)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x, _y + 1, level, "d")
                level += bullet
                moveDelay = 0.0
            }
        }
        if (keysHeld(KeyData.D)){
            if(moveDelay >= moveInterval){
                var bullet = new Bullet(_x + 1, _y, level, "r")
                level += bullet
                moveDelay = 0.0
            }
        }
    }

    def stillHere(): Boolean = {
        var ret = true
        if(level.ghosts.length > 0){
            for(i <- 0 until level.ghosts.length){
                if(Entity.intersect(this, level.ghosts(i))){
                    ret = false
                }
            }
        }

        if(level.fire.length > 0){
            for(i <- 0 until level.fire.length){
                if(Entity.intersect(this, level.fire(i))){
                    ret = false
                }
            }
        }
        return ret
    }

    def buildPassable = PassableEntity(800, _x, _y, width, height)

}